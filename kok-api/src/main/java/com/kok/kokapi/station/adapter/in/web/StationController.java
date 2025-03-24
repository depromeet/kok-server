package com.kok.kokapi.station.adapter.in.web;

import com.kok.kokapi.common.response.ApiResponseDto;
import com.kok.kokapi.config.annotion.V1Controller;
import com.kok.kokapi.station.adapter.in.dto.response.RecommendedStationResponse;
import com.kok.kokcore.station.usecase.RecommendStationUseCase;
import com.kok.kokcore.station.usecase.RetrieveRouteUseCase;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@V1Controller
@RequiredArgsConstructor
public class StationController {

    private final RecommendStationUseCase recommendStationUseCase;
    private final RetrieveRouteUseCase retrieveRouteUseCase;

    @Operation(summary = "지하철역 추천", description = "Recommend subway stations based on the user's location.")
    @GetMapping("/stations/recommend/{roomId}")
    public ResponseEntity<ApiResponseDto<List<RecommendedStationResponse>>> recommendStations(
        @PathVariable String roomId) {

        List<RecommendedStationResponse> recommendedStations = recommendStationUseCase.recommendStations(
                roomId).stream()
            .map(station ->
                RecommendedStationResponse.of(station,
                    retrieveRouteUseCase.retrieveRoutes(station)))
            .toList();

        return ResponseEntity.ok(ApiResponseDto.success(recommendedStations));
    }

}
