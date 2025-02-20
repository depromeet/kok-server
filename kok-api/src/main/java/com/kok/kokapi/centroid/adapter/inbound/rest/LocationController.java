package com.kok.kokapi.centroid.adapter.inbound.rest;

import com.kok.kokapi.centroid.adapter.inbound.dto.LocationRequest;
import com.kok.kokapi.centroid.adapter.outbound.dto.CentroidResponse;
import com.kok.kokapi.centroid.adapter.outbound.dto.LocationResponse;
import com.kok.kokapi.common.response.ApiResponseDto;
import com.kok.kokapi.config.geometry.GeometryConfig;
import com.kok.kokcore.application.domain.entity.Location;
import com.kok.kokcore.application.usecase.CreateLocationUsecase;
import com.kok.kokcore.application.usecase.ReadCentroidUsecase;
import com.kok.kokcore.application.usecase.ReadLocationUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/location")
public class LocationController {

    private final CreateLocationUsecase createLocationUsecase;
    private final ReadCentroidUsecase readCentroidUsecase;
    private final ReadLocationUsecase readLocationUsecase;
    private final GeometryConfig.PointConverter pointConverter;

    @PostMapping("/create")
    public ResponseEntity<ApiResponseDto<CentroidResponse>> createLocation(@RequestBody LocationRequest locationRequest) {
        Location location = createLocationUsecase.createLocation(
                locationRequest.uuid(),
                locationRequest.memberId(),
                pointConverter.fromCoordinates(
                        locationRequest.latitude(),
                        locationRequest.longitude()
                )
        );
        if (location == null) {
            throw new IllegalArgumentException("위치 생성에 실패했습니다.");
        }

        Pair<BigDecimal, BigDecimal> centroid = pointConverter.toCoordinates(readCentroidUsecase.readCentroid(locationRequest.uuid()));

        return ResponseEntity.ok(ApiResponseDto.success(
                CentroidResponse.of(locationRequest.uuid(), centroid.getFirst(), centroid.getSecond())
        ));
    }

    @GetMapping("/centroid/{uuid}")
    public ResponseEntity<ApiResponseDto<CentroidResponse>> getCentroid(@PathVariable String uuid) {
        Pair<BigDecimal, BigDecimal> centroid = pointConverter.toCoordinates(readCentroidUsecase.readCentroid(uuid));

        if (centroid == null) {
            throw new IllegalArgumentException("해당 UUID에 대한 중심점을 찾을 수 없습니다.");
        }

        return ResponseEntity.ok(ApiResponseDto.success(
                CentroidResponse.of(uuid, centroid.getFirst(), centroid.getSecond())
        ));
    }

    @GetMapping("/{uuid}/{memberId}")
    public ResponseEntity<ApiResponseDto<LocationResponse>> getLocation(@PathVariable String uuid, @PathVariable Integer memberId) {
        Location location = readLocationUsecase.readLocation(uuid, memberId);
        if (location == null) {
            throw new IllegalArgumentException("해당 멤버의 위치를 찾을 수 없습니다.");
        }

        Pair<BigDecimal, BigDecimal> locationPair = pointConverter.toCoordinates(location.getPoint());

        return ResponseEntity.ok(ApiResponseDto.success(
                LocationResponse.of(uuid, memberId, locationPair.getFirst(), locationPair.getSecond())
        ));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<ApiResponseDto<List<LocationResponse>>> getLocations(@PathVariable String uuid) {
        List<Location> locations = readLocationUsecase.readLocations(uuid);

        if (locations.isEmpty()) {
            throw new IllegalArgumentException("해당 UUID에 대한 위치들을 찾을 수 없습니다.");
        }

        List<LocationResponse> responses = locations.stream()
                .map(location -> {
                    Pair<BigDecimal, BigDecimal> coordinates = pointConverter.toCoordinates(location.getPoint());
                    return LocationResponse.of(
                            uuid,
                            location.getMemberId(),
                            coordinates.getFirst(),
                            coordinates.getSecond()
                    );
                })
                .toList();

        return ResponseEntity.ok(ApiResponseDto.success(responses));
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponseDto<LocationResponse>> updateLocation(@RequestBody LocationRequest locationRequest) {
        Location location = createLocationUsecase.UpdateLocation(
                locationRequest.uuid(),
                locationRequest.memberId(),
                pointConverter.fromCoordinates(
                        locationRequest.latitude(),
                        locationRequest.longitude()
                )
        );
        Pair<BigDecimal, BigDecimal> locationPair = pointConverter.toCoordinates(location.getPoint());

        return ResponseEntity.ok(ApiResponseDto.success(
                LocationResponse.of(locationRequest.uuid(), locationRequest.memberId(), locationPair.getFirst(), locationPair.getSecond())
        ));
    }
}
