package com.kok.kokapi.centroid.adapter.in.rest;

import com.kok.kokapi.adapter.in.web.BaseController;
import com.kok.kokapi.centroid.adapter.in.dto.LocationRequest;
import com.kok.kokapi.centroid.adapter.out.dto.CentroidResponse;
import com.kok.kokapi.centroid.adapter.out.dto.LocationResponse;
import com.kok.kokapi.centroid.adapter.out.mapper.LocationMapper;
import com.kok.kokapi.common.response.ApiResponseDto;
import com.kok.kokcore.application.domain.entity.Location;
import com.kok.kokcore.application.usecase.CreateLocationUsecase;
import com.kok.kokcore.application.usecase.ReadCentroidUsecase;
import com.kok.kokcore.application.usecase.ReadLocationUsecase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/location")
public class LocationController extends BaseController {

    private final CreateLocationUsecase createLocationUsecase;
    private final ReadCentroidUsecase readCentroidUsecase;
    private final ReadLocationUsecase readLocationUsecase;
    private final LocationMapper locationMapper;

    @PostMapping("/create")
    public ResponseEntity<ApiResponseDto<CentroidResponse>> createLocation(@Valid @RequestBody LocationRequest locationRequest) {
        createLocationUsecase.createLocation(
                locationRequest.uuid(),
                locationRequest.memberId(),
                locationRequest.latitude(),
                locationRequest.longitude()
        );

        // 🔥 변환 로직을 서비스에서 수행하도록 변경
        Pair<BigDecimal, BigDecimal> centroid = readCentroidUsecase.readCentroidCoordinates(locationRequest.uuid());

        return ResponseEntity.ok(ApiResponseDto.success(
                CentroidResponse.of(locationRequest.uuid(), centroid.getFirst(), centroid.getSecond())
        ));
    }

    @GetMapping("/centroid/{uuid}")
    public ResponseEntity<ApiResponseDto<CentroidResponse>> getCentroid(@PathVariable String uuid) {
        Pair<BigDecimal, BigDecimal> centroid = readCentroidUsecase.readCentroidCoordinates(uuid);

        return ResponseEntity.ok(ApiResponseDto.success(
                CentroidResponse.of(uuid, centroid.getFirst(), centroid.getSecond())
        ));
    }

    @GetMapping("/{uuid}/{memberId}")
    public ResponseEntity<ApiResponseDto<LocationResponse>> getLocation(@PathVariable String uuid, @PathVariable Integer memberId) {
        Location location = readLocationUsecase.readLocation(uuid, memberId);

        return ResponseEntity.ok(ApiResponseDto.success(locationMapper.toResponse(location)));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<ApiResponseDto<List<LocationResponse>>> getLocations(@PathVariable String uuid) {
        List<LocationResponse> responses = locationMapper.toResponseList(readLocationUsecase.readLocations(uuid));

        return ResponseEntity.ok(ApiResponseDto.success(responses));
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponseDto<LocationResponse>> updateLocation(@Valid @RequestBody LocationRequest locationRequest) {
        Location location = createLocationUsecase.updateLocation(
                locationRequest.uuid(),
                locationRequest.memberId(),
                locationRequest.latitude(),
                locationRequest.longitude()
        );
        Pair<BigDecimal, BigDecimal> locationPair = readCentroidUsecase.readCentroidCoordinates(locationRequest.uuid());

        return ResponseEntity.ok(ApiResponseDto.success(
                LocationResponse.of(locationRequest.uuid(), locationRequest.memberId(), locationPair.getFirst(), locationPair.getSecond())
        ));
    }
}

