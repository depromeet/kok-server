package com.kok.kokapi.public_transportation.adapter.in.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kok.kokapi.common.response.ApiResponseDto;
import com.kok.kokapi.config.annotion.V1Controller;
import com.kok.kokapi.public_transportation.adapter.in.dto.RouteRequest;
import com.kok.kokapi.public_transportation.adapter.in.dto.TmapPublicTransportationParsedResponse;
import com.kok.kokapi.public_transportation.adapter.in.dto.TmapComplexPublicTransportationParsedResponse;
import com.kok.kokcore.public_transfortation.usecase.RetrievePublicTransportationUsecase;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@V1Controller
@RequiredArgsConstructor
public class PublicTransportationController {

    private final RetrievePublicTransportationUsecase retrievePublicTransportationUsecase;
    private final ObjectMapper objectMapper;

    @Operation(summary = "대중교통 조회", description = "Retrieve the total time and transfer count for a route using the station ID")
    @PostMapping("/route/{stationId}")
    public ResponseEntity<ApiResponseDto<TmapPublicTransportationParsedResponse>> getPublicTransportation(@PathVariable Long stationId, @RequestBody RouteRequest routeRequest) {
        try {
            TmapPublicTransportationParsedResponse publicTransportation = objectMapper.readValue(retrievePublicTransportationUsecase.retrievePublicTransportation(stationId, routeRequest.UUID(), routeRequest.memberId())
            ,TmapPublicTransportationParsedResponse.class);
            return ResponseEntity.ok(ApiResponseDto.success(publicTransportation));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("파싱 실패..");
        }
    }

    @Operation(summary = "대중교통 전문 조회", description = "Retrieve the total time and transfer count for a route using the station ID")
    @PostMapping("/route/complex/{stationId}")
    public ResponseEntity<ApiResponseDto<TmapComplexPublicTransportationParsedResponse>> getComplexPublicTransportation(@PathVariable Long stationId, @RequestBody RouteRequest routeRequest) {
        try {
            TmapComplexPublicTransportationParsedResponse publicTransportation = objectMapper.readValue(retrievePublicTransportationUsecase.retrieveComplexPublicTransportation(stationId, routeRequest.UUID(), routeRequest.memberId())
                    , TmapComplexPublicTransportationParsedResponse.class);
            return ResponseEntity.ok(ApiResponseDto.success(publicTransportation));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("파싱 실패..");
        }
    }

}
