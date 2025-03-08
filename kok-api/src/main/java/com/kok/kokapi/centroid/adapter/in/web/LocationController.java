package com.kok.kokapi.centroid.adapter.in.web;

import com.kok.kokapi.centroid.adapter.in.dto.request.LocationRequest;
import com.kok.kokapi.centroid.adapter.in.dto.response.CentroidResponse;
import com.kok.kokapi.centroid.adapter.in.dto.response.ConvexHullLocationResponse;
import com.kok.kokapi.centroid.adapter.in.dto.response.LocationResponse;
import com.kok.kokapi.centroid.adapter.out.mapper.LocationMapper;
import com.kok.kokapi.common.response.ApiResponseDto;
import com.kok.kokapi.config.annotion.V1Controller;
import com.kok.kokcore.location.domain.Location;
import com.kok.kokcore.location.usecase.CreateLocationUsecase;
import com.kok.kokcore.location.usecase.ReadCentroidUsecase;
import com.kok.kokcore.location.usecase.ReadLocationUsecase;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@V1Controller
@RequiredArgsConstructor
public class LocationController {

    private final CreateLocationUsecase createLocationUsecase;
    private final ReadCentroidUsecase readCentroidUsecase;
    private final ReadLocationUsecase readLocationUsecase;
    private final LocationMapper locationMapper;

    @Operation(summary = "위치 입력", description = "Create a new location with the provided details.")
    @PostMapping("/locations")
    public ResponseEntity<ApiResponseDto<CentroidResponse>> createLocation(@Valid @RequestBody LocationRequest locationRequest) {
        createLocationUsecase.createLocation(
                locationRequest.uuid(),
                locationRequest.memberId(),
                locationRequest.latitude(),
                locationRequest.longitude()
        );

        Pair<BigDecimal, BigDecimal> centroid = readCentroidUsecase.readCentroidCoordinates(locationRequest.uuid());

        return ResponseEntity.ok(ApiResponseDto.success(
                CentroidResponse.of(locationRequest.uuid(), centroid.getFirst(), centroid.getSecond())
        ));
    }

    // For Test
    @Operation(summary = "중심 좌표 조회", description = "Retrieve the centroid coordinates for a location using its UUID")
    @GetMapping("/locations/centroid/{uuid}")
    public ResponseEntity<ApiResponseDto<CentroidResponse>> getCentroid(@PathVariable String uuid) {
        Pair<BigDecimal, BigDecimal> centroid = readCentroidUsecase.readCentroidCoordinates(uuid);

        return ResponseEntity.ok(ApiResponseDto.success(
                CentroidResponse.of(uuid, centroid.getFirst(), centroid.getSecond())
        ));
    }

    @Operation(summary = "위치 조회 Basic", description = "Retrieve detailed information for a location using its UUID and member ID")
    @GetMapping("/locations/{uuid}/{memberId}")
    public ResponseEntity<ApiResponseDto<LocationResponse>> getLocation(@PathVariable String uuid, @PathVariable Integer memberId) {
        Location location = readLocationUsecase.readLocation(uuid, memberId);

        return ResponseEntity.ok(ApiResponseDto.success(locationMapper.toResponse(location)));
    }

    @Operation(summary = "위치조회 ConvexHull", description = "Retrieve the ConvexHull inside list, outside list of locations for a UUID")
    @GetMapping("/locations/ConvH/{uuid}")
    public ResponseEntity<ApiResponseDto<ConvexHullLocationResponse>> getConvexHullLocations(@PathVariable String uuid){
        List<LocationResponse> convexHull = locationMapper.toResponseList(readLocationUsecase.readConvexHull(uuid));
        List<LocationResponse> inside = locationMapper.toResponseList(readLocationUsecase.readInsideConvexHull(uuid));

        return ResponseEntity.ok(ApiResponseDto.success(ConvexHullLocationResponse.of(convexHull, inside )));
    }

    @Operation(summary = "위치 목록 조회", description = "Retrieve the list of locations for a UUID")
    @GetMapping("/locations/{uuid}")
    public ResponseEntity<ApiResponseDto<List<LocationResponse>>> getLocations(@PathVariable String uuid) {
        List<LocationResponse> responses = locationMapper.toResponseList(readLocationUsecase.readLocations(uuid));

        return ResponseEntity.ok(ApiResponseDto.success(responses));
    }

    @Operation(summary = "위치 수정", description = "Update the location with the provided details.")
    @PutMapping("/locations")
    public ResponseEntity<ApiResponseDto<LocationResponse>> updateLocation(@Valid @RequestBody LocationRequest locationRequest) {
        Location location = createLocationUsecase.updateLocation(
                locationRequest.uuid(),
                locationRequest.memberId(),
                locationRequest.latitude(),
                locationRequest.longitude()
        );
        LocationResponse response = locationMapper.toResponse(location);

        return ResponseEntity.ok(ApiResponseDto.success(response));
    }
}

