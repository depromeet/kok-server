package com.kok.kokapi.station.application.service;

import com.kok.kokapi.station.adapter.in.dto.response.RecommendedStationResponse;
import com.kok.kokcore.station.domain.entity.Station;
import com.kok.kokcore.station.usecase.RetrieveRouteUseCase;
import com.kok.kokcore.station.usecase.SystemRecommendUseCase;
import com.kok.kokcore.station.usecase.UserRecommendUseCase;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StationFacadeService {

    private final SystemRecommendUseCase systemRecommendedUseCase;
    private final UserRecommendUseCase userRecommendUseCase;
    private final RetrieveRouteUseCase retrieveRouteUseCase;

    public List<RecommendedStationResponse> getCandidateStationResponse(String roomId) {

        return getCandidateStation(roomId).stream()
            .map(station ->
                RecommendedStationResponse.of(station,
                    retrieveRouteUseCase.retrieveRoutes(station)))
            .toList();
    }

    public Set<Station> getCandidateStation(String roomId) {
        List<Station> recommendedStations = systemRecommendedUseCase.systemRecommendStation(roomId);
        List<Station> customStations = userRecommendUseCase.getUserRecommendStation(roomId);

        return Stream.concat(
            recommendedStations.stream(),
            customStations.stream()
        ).collect(Collectors.toSet());
    }
}
