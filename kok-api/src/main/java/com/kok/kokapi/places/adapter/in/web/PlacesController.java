package com.kok.kokapi.places.adapter.in.web;

import com.kok.kokapi.common.response.ApiResponseDto;
import com.kok.kokapi.config.annotion.V1Controller;
import com.kok.kokapi.places.adapter.in.dto.request.PlacesRequest;
import com.kok.kokapi.places.adapter.in.dto.response.PlacesResponse;
import com.kok.kokcore.places.application.port.in.PlaceInput;
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
        PlaceInput input = new PlaceInput(
                request.placeType(),
                request.latitude(),
                request.longitude(),
                request.maxCount()
        );
        PlacesResult result = searchPlaceUseCase.getPlaces(input);
        PlacesResponse response = mapToResponse(result);
        return ResponseEntity.ok(ApiResponseDto.success(response));
    }

    private PlacesResponse mapToResponse(PlacesResult result) {
        PlacesResponse response = new PlacesResponse();
        response.setPlaceResponses(result.getPlaces().stream().map(place -> {
            PlacesResponse.PlaceResponse pr = new PlacesResponse.PlaceResponse();
            pr.setDisplayName(place.getName());
            pr.setFormattedAddress(place.getAddress());
            PlacesResponse.PlaceResponse.Location loc = new PlacesResponse.PlaceResponse.Location();
            loc.setLatitude(place.getLatitude());
            loc.setLongitude(place.getLongitude());
            pr.setLocation(loc);
            return pr;
        }).toList());
        return response;
    }
}
