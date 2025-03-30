package com.kok.kokapi.vote.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kok.kokapi.public_transportation.adapter.in.dto.response.TmapPublicTransportationParsedResponse;
import com.kok.kokapi.public_transportation.application.service.TmapPublicTransportationService;
import com.kok.kokapi.vote.adapter.in.dto.request.VoteRequest;
import com.kok.kokapi.vote.adapter.in.dto.response.CandidateResponse;
import com.kok.kokapi.vote.adapter.in.dto.response.MemberVoteStatusResponse;
import com.kok.kokapi.vote.adapter.in.dto.response.VoteResultResponse;
import com.kok.kokcore.room.domain.Member;
import com.kok.kokcore.room.usecase.GetRoomUseCase;
import com.kok.kokcore.station.domain.entity.Route;
import com.kok.kokcore.station.domain.entity.Station;
import com.kok.kokcore.station.usecase.GetStationUseCase;
import com.kok.kokcore.station.usecase.RetrieveRouteUseCase;
import com.kok.kokcore.vote.domain.Candidate;
import com.kok.kokcore.vote.domain.Vote;
import com.kok.kokcore.vote.domain.vo.VoteStatus;
import com.kok.kokcore.vote.usecase.GetCandidateUseCase;
import com.kok.kokcore.vote.usecase.GetVoteUseCase;
import com.kok.kokcore.vote.usecase.SaveVoteUseCase;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VoteFacadeService {

    private final GetCandidateUseCase getCandidateUseCase;
    private final GetStationUseCase getStationUseCase;
    private final RetrieveRouteUseCase retrieveRouteUseCase;
    private final SaveVoteUseCase saveVoteUseCase;
    private final GetVoteUseCase getVoteUseCase;
    private final GetRoomUseCase getRoomUseCase;
    private final TmapPublicTransportationService tmapPublicTransportationService;
    private final ObjectMapper objectMapper;

    public List<CandidateResponse> getCandidates(String roomId, String memberId,
        List<Station> stations) {
        List<Candidate> candidates = getCandidateUseCase.saveAndGetCandidates(roomId, stations);
        List<CandidateResponse> responses = new ArrayList<>();
        for (Candidate candidate : candidates) {
            Station station = getStationUseCase.getStation(candidate.getStationId());
            List<Route> routes = retrieveRouteUseCase.retrieveRoutes(station);
            TmapPublicTransportationParsedResponse transportationParsedResponse = getTransportationParsedResponse(
                roomId, memberId, station);
            CandidateResponse response = CandidateResponse.of(
                station, routes, transportationParsedResponse, List.of());
            responses.add(response);
        }
        return responses;
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

    public void createVote(String roomId, String memberId, VoteRequest voteRequest) {
        List<Vote> votes = getVotes(roomId, memberId, voteRequest);
        saveVoteUseCase.saveVotes(votes);
    }

    private List<Vote> getVotes(String roomId, String memberId, VoteRequest voteRequest) {
        List<Vote> votes = new ArrayList<>();
        List<Long> agreedStationIds = voteRequest.agreedStationIds();
        List<Candidate> candidates = getCandidateUseCase.getCandidates(roomId);
        for (Candidate candidate : candidates) {
            if (isAgree(agreedStationIds, candidate)) {
                votes.add(new Vote(candidate, memberId, VoteStatus.AGREE));
                continue;
            }
            votes.add(new Vote(candidate, memberId, VoteStatus.DISAGREE));
        }
        return votes;
    }

    private static boolean isAgree(List<Long> agreedStationIds, Candidate candidate) {
        return agreedStationIds.contains(candidate.getStationId());
    }

    public List<MemberVoteStatusResponse> getMemberVoteStatus(String roomId) {
        List<Member> members = getRoomUseCase.getParticipants(roomId);
        List<MemberVoteStatusResponse> responses = new ArrayList<>();
        for (Member member : members) {
            boolean isVoted = getVoteUseCase.isVotedByMember(roomId, member.getMemberId());
            MemberVoteStatusResponse response = MemberVoteStatusResponse.of(member, isVoted);
            responses.add(response);
        }
        return responses;
    }

    public VoteResultResponse getVoteResult(String roomId, String memberId) {
        return null;
    }
}
