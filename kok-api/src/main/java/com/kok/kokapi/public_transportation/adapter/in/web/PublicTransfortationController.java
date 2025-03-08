package com.kok.kokapi.public_transportation.adapter.in.web;

import com.kok.kokapi.common.response.ApiResponseDto;
import com.kok.kokapi.config.annotion.V1Controller;
import com.kok.kokapi.public_transportation.adapter.in.dto.RouteRequest;
import com.kok.kokapi.public_transportation.adapter.in.dto.RouteResponse;
import com.kok.kokcore.public_transfortation.usecase.RetrievePublicTransportationUsecase;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@V1Controller
@RequiredArgsConstructor
public class PublicTransfortationController {

    private final RetrievePublicTransportationUsecase retrievePublicTransportationUsecase;

    @Operation(summary = "대중교통 조회", description = "Retrieve the total time and transfer count for a route using the station ID")
    @PostMapping("/route/{stationId}")
    public ResponseEntity<ApiResponseDto<RouteResponse>> getPublicTransportation(@PathVariable Long stationId, @RequestBody RouteRequest routeRequest) {
        List<Integer> publicTransportation =
                retrievePublicTransportationUsecase.retrievePublicTransportation(stationId, routeRequest.UUID(), routeRequest.memberId());

        return ResponseEntity.ok(ApiResponseDto.success(
                RouteResponse.of(publicTransportation.get(0), publicTransportation.get(1))
        ));
    }

}
