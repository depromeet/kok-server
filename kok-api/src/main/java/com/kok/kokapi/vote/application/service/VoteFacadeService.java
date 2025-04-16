package com.kok.kokapi.vote.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kok.kokapi.public_transportation.adapter.in.dto.response.TmapPublicTransportationParsedResponse;
import com.kok.kokapi.public_transportation.application.service.TmapPublicTransportationService;
import com.kok.kokapi.vote.adapter.in.dto.response.CandidateResponse;
import com.kok.kokapi.vote.adapter.in.dto.response.MemberVoteStatusResponse;
import com.kok.kokapi.vote.adapter.in.dto.response.ResultResponse;
import com.kok.kokapi.vote.adapter.in.dto.response.VoteCurrentResultResponse;
import com.kok.kokcore.location.domain.Location;
import com.kok.kokcore.location.usecase.ReadLocationUseCase;
import com.kok.kokcore.room.domain.Member;
import com.kok.kokcore.room.domain.Room;
import com.kok.kokcore.room.usecase.GetRoomUseCase;
import com.kok.kokcore.room.usecase.UpdateRoomUseCase;
import com.kok.kokcore.station.domain.entity.Route;
import com.kok.kokcore.station.domain.entity.Station;
import com.kok.kokcore.station.usecase.GetStationUseCase;
import com.kok.kokcore.station.usecase.RetrieveRouteUseCase;
import com.kok.kokcore.station.usecase.SystemRecommendUseCase;
import com.kok.kokcore.station.usecase.UserRecommendUseCase;
import com.kok.kokcore.vote.VoteResults;
import com.kok.kokcore.vote.domain.VoteResult;
import com.kok.kokcore.vote.usecase.GetCandidateUseCase;
import com.kok.kokcore.vote.usecase.GetVoteUseCase;
import com.kok.kokcore.vote.usecase.SaveVoteUseCase;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VoteFacadeService {

    private final GetCandidateUseCase getCandidateUseCase;
    private final RetrieveRouteUseCase retrieveRouteUseCase;
    private final GetVoteUseCase getVoteUseCase;
    private final GetRoomUseCase getRoomUseCase;
    private final TmapPublicTransportationService tmapPublicTransportationService;
    private final ObjectMapper objectMapper;
    private final SaveVoteUseCase saveVoteUseCase;
    private final ReadLocationUseCase readLocationUseCase;
    private final SystemRecommendUseCase systemRecommendUseCase;
    private final UserRecommendUseCase userRecommendUseCase;
    private final GetStationUseCase getStationUseCase;
    private final UpdateRoomUseCase updateRoomUseCase;

    public List<CandidateResponse> getCandidates(String roomId, String memberId) {
        List<Station> recommendedStations = systemRecommendUseCase.systemRecommendStation(roomId);
        List<Station> customStations = userRecommendUseCase.getUserRecommendStation(roomId);
        Set<Station> stations = Stream.concat(
            recommendedStations.stream(), customStations.stream()
        ).collect(Collectors.toSet());
        getCandidateUseCase.saveAndGetCandidates(roomId, stations);

        List<CandidateResponse> responses = new ArrayList<>();
        for (Station station : stations) {
            if (recommendedStations.contains(station)) {
                responses.add(createCandidateResponse(station, roomId, memberId, true));
                continue;
            }
            if (customStations.contains(station)) {
                responses.add(createCandidateResponse(station, roomId, memberId, false));
            }
        }

        return responses;
    }


    private CandidateResponse createCandidateResponse(
        Station station, String roomId, String memberId, boolean isRecommended) {
        List<Route> routes = retrieveRouteUseCase.retrieveRoutes(station);
        TmapPublicTransportationParsedResponse transportation = getTransportationParsedResponse(
            roomId, memberId, station);
        CandidateResponse response = isRecommended
            ? CandidateResponse.recommended(station, routes, transportation, List.of())
            : CandidateResponse.custom(station, routes, transportation, List.of());
        return response;
    }

    private TmapPublicTransportationParsedResponse getTransportationParsedResponse(
        String roomId, String memberId, Station station) {
        String content = tmapPublicTransportationService.retrievePublicTransportation(
            station.getId(),
            roomId,
            memberId
        );
        try {
            return objectMapper.readValue(content, TmapPublicTransportationParsedResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(
                "Failed parsing into \"TmapPublicTransportationParsedResponse\" for " + content);
        }
    }

    public List<MemberVoteStatusResponse> getMemberVoteStatus(String roomId) {
        List<Member> members = getRoomUseCase.getParticipants(roomId);
        List<MemberVoteStatusResponse> responses = new ArrayList<>();
        for (Member member : members) {
            boolean isVoted = getVoteUseCase.isVotedByMember(roomId, member.getMemberId());
            Location location = readLocationUseCase.readLocation(roomId, member.getMemberId());
            MemberVoteStatusResponse response = MemberVoteStatusResponse.of(
                member, location, isVoted);
            responses.add(response);
        }
        return responses;
    }

    public VoteCurrentResultResponse getVoteCurrentResult(String roomId) {
        List<ResultResponse> responses = new ArrayList<>();
        Room room = updateRoomUseCase.updateRoomStatus(roomId, LocalDateTime.now());
        int votedCount = getVoteUseCase.countVotedMembers(roomId);
        VoteResults voteResults = getVoteUseCase.getVoteResultsByRoomId(roomId);
        for (VoteResult voteResult : voteResults.getVoteResults()) {
            Station station = getStationUseCase.getStation(voteResult.getStationId());
            List<Member> members = getRoomUseCase.getParticipantsByRoomIdInMemberIds(
                roomId, voteResult.getMemberIds());
            responses.add(ResultResponse.of(station, voteResult, members));
        }
        return new VoteCurrentResultResponse(room.getNotVotedCount(votedCount), responses);
    }

    public void saveVotes(String roomId, String memberId, List<Long> agreedStationIds) {
        saveVoteUseCase.saveVotes(roomId, memberId, agreedStationIds);
        updateRoomUseCase.updateRoomStatus(roomId, LocalDateTime.now());
    }

    public int countCandidates(String roomId) {
        List<Station> recommendedStations = systemRecommendUseCase.systemRecommendStation(roomId);
        List<Station> customStations = userRecommendUseCase.getUserRecommendStation(roomId);
        Set<Station> stations = Stream.concat(
            recommendedStations.stream(), customStations.stream()
        ).collect(Collectors.toSet());
        return getCandidateUseCase.saveAndGetCandidates(roomId, stations).size();
    }
}
