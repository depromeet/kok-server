package com.kok.kokapi.station.application.service;

import com.kok.kokapi.station.adapter.in.dto.response.RecommendedStationResponse;
import com.kok.kokcore.station.usecase.CustomStationUseCase;
import com.kok.kokcore.station.usecase.RecommendStationUseCase;
import com.kok.kokcore.station.usecase.RetrieveRouteUseCase;
import com.kok.kokcore.station.domain.entity.Station;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StationFacadeService {

    private final RecommendStationUseCase recommendStationUseCase;
    private final CustomStationUseCase customStationUseCase;
    private final RetrieveRouteUseCase retrieveRouteUseCase;

    public List<RecommendedStationResponse> getCandidateStationResponse(String roomId) {

        return getCandidateStation(roomId).stream()
            .map(station ->
                RecommendedStationResponse.of(station, retrieveRouteUseCase.retrieveRoutes(station)))
            .toList();
    }

    public List<Station> getCandidateStation(String roomId) {
        List<Station> recommendedStations = recommendStationUseCase.recommendStations(roomId);
        List<Station> customStations = customStationUseCase.getCustomRecommendedStations(roomId);

        return Stream.concat(
            recommendedStations.stream(),
            customStations.stream()
        ).toList();
    }
}
