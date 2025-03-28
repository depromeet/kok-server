package com.kok.kokapi.places.adapter.in.web;

import com.kok.kokapi.common.response.ApiResponseDto;
import com.kok.kokapi.config.annotion.V1Controller;
import com.kok.kokapi.places.adapter.in.dto.request.PlacesRequest;
import com.kok.kokapi.places.adapter.in.dto.response.PlacesResponse;
import com.kok.kokcore.places.application.usecase.SearchPlaceUseCase;
import com.kok.kokcore.places.domain.model.PlacesResult;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@V1Controller
@RequiredArgsConstructor
public class PlacesController {

    private final SearchPlaceUseCase searchPlaceUseCase;

    @Operation(summary = "주변 플레이스 조회", description = "추천 장소 주변의 핫플레이스 목록을 조회합니다.")
    @PostMapping("/search/places")
    public ResponseEntity<ApiResponseDto<PlacesResponse>> searchPlaces(@RequestBody PlacesRequest request) throws Exception {
        PlacesResult result = searchPlaceUseCase.getPlaces(request.toPlaceInput());
        PlacesResponse response = new PlacesResponse(result);
        return ResponseEntity.ok(ApiResponseDto.success(response));
    }
}
