package com.kok.kokapi.vote.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kok.kokapi.public_transportation.adapter.in.dto.TmapPublicTransportationParsedResponse;
import com.kok.kokapi.public_transportation.application.service.TmapPublicTransportationService;
import com.kok.kokapi.vote.adapter.in.dto.response.CandidateResponse;
import com.kok.kokcore.station.application.usecase.GetRecommendStationUseCase;
import com.kok.kokcore.station.application.usecase.GetStationUseCase;
import com.kok.kokcore.station.application.usecase.RetrieveRouteUseCase;
import com.kok.kokcore.station.domain.entity.Route;
import com.kok.kokcore.station.domain.entity.Station;
import com.kok.kokcore.vote.domain.Candidate;
import com.kok.kokcore.vote.usecase.GetCandidateUseCase;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VoteFacadeService {

    private final GetCandidateUseCase getCandidateUseCase;
    private final GetRecommendStationUseCase getRecommendStationUseCase;
    private final GetStationUseCase getStationUseCase;
    private final RetrieveRouteUseCase retrieveRouteUseCase;
    private final TmapPublicTransportationService tmapPublicTransportationService;
    private final ObjectMapper objectMapper;

    public List<CandidateResponse> getCandidates(String roomId, String memberId) {
        List<CandidateResponse> responses = new ArrayList<>();
        List<Station> stations = getRecommendStationUseCase.getRecommendedStations(roomId);
        List<Candidate> candidates = getCandidateUseCase.getCandidate(roomId, stations);
        for (Candidate candidate : candidates) {
            Station station = getStationUseCase.getStation(candidate.getStationId());
            List<Route> routes = retrieveRouteUseCase.retrieveRoutes(station);
            TmapPublicTransportationParsedResponse transportationParsedResponse = getTransportationParsedResponse(
                roomId, memberId, station);
            CandidateResponse.of(station, routes, transportationParsedResponse, List.of());
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

}
