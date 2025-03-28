package com.kok.kokapi.station.adapter.in.web;

import com.kok.kokapi.common.response.ApiResponseDto;
import com.kok.kokapi.config.annotion.V1Controller;
import com.kok.kokapi.station.adapter.in.dto.response.RecommendedStationResponse;
import com.kok.kokapi.station.application.service.StationFacadeService;
import com.kok.kokcore.station.domain.entity.Station;
import com.kok.kokcore.station.usecase.CustomStationUseCase;
import com.kok.kokcore.station.usecase.RecommendStationUseCase;
import com.kok.kokcore.station.usecase.RetrieveRouteUseCase;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@V1Controller
@RequiredArgsConstructor
public class StationController {

    private final RecommendStationUseCase recommendStationUseCase;
    private final CustomStationUseCase customStationUseCase;
    private final RetrieveRouteUseCase retrieveRouteUseCase;
    private final StationFacadeService stationFacadeService;

    @Operation(summary = "추천 지하철역 리턴", description = "Recommend subway stations based on the user's location.")
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

    @Operation(summary = "지하철역 검색", description = "Search for subway stations based on the keyword.")
    @GetMapping("/stations/search/{keyword}")
    public ResponseEntity<ApiResponseDto<List<RecommendedStationResponse>>> searchStations(
        @PathVariable String keyword) {

        List<RecommendedStationResponse> recommendedStations = customStationUseCase.searchStations(
                keyword).stream()
            .map(station ->
                RecommendedStationResponse.of(station,
                    retrieveRouteUseCase.retrieveRoutes(station)))
            .toList();

        return ResponseEntity.ok(ApiResponseDto.success(recommendedStations));
    }

    @Operation(summary = "지하철역 추가", description = "Add a subway station to the user's custom list.")
    @PostMapping("/stations/custom/{roomId}/{stationId}")
    public ResponseEntity<ApiResponseDto<RecommendedStationResponse>> addCustomStations(
        @PathVariable String roomId, @PathVariable Long stationId) {
        Station station = customStationUseCase.addCustomStations(roomId, stationId);
        return ResponseEntity.ok(ApiResponseDto.success(
            RecommendedStationResponse.of(station, retrieveRouteUseCase.retrieveRoutes(station))));
    }

    @Operation(summary = "투표할 지하철역 리스트 리턴", description = "Return a list of subway stations to vote on.")
    @GetMapping("/stations/candidate/{roomId}")
    public ResponseEntity<ApiResponseDto<List<RecommendedStationResponse>>> getCustomRecommendedStations(
        @PathVariable String roomId) {
        List<RecommendedStationResponse> candidateStations = stationFacadeService.getCandidateStationResponse(
            roomId);
        return ResponseEntity.ok(ApiResponseDto.success(candidateStations));
    }
}
