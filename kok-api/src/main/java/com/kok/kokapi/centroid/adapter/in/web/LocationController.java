package com.kok.kokapi.centroid.adapter.in.web;

import com.kok.kokapi.centroid.adapter.in.dto.request.LocationRequest;
import com.kok.kokapi.centroid.adapter.in.dto.response.CentroidResponse;
import com.kok.kokapi.centroid.adapter.in.dto.response.ConvexHullLocationResponse;
import com.kok.kokapi.centroid.adapter.in.dto.response.LocationResponse;
import com.kok.kokapi.centroid.adapter.out.mapper.LocationMapper;
import com.kok.kokapi.common.response.ApiResponseDto;
import com.kok.kokapi.config.annotion.V1Controller;
import com.kok.kokcore.location.domain.Location;
import com.kok.kokcore.location.usecase.CreateLocationUseCase;
import com.kok.kokcore.location.usecase.LoadCentroidUseCase;
import com.kok.kokcore.location.usecase.ReadLocationUseCase;
import com.kok.kokcore.room.domain.Member;
import com.kok.kokcore.room.usecase.GetRoomUseCase;
import com.kok.kokcore.room.usecase.UpdateRoomUseCase;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@V1Controller
@RequiredArgsConstructor
public class LocationController {

    private final CreateLocationUseCase createLocationUsecase;
    private final LoadCentroidUseCase loadCentroidUsecase;
    private final ReadLocationUseCase readLocationUsecase;
    private final GetRoomUseCase getRoomUseCase;
    private final UpdateRoomUseCase updateRoomUseCase;
    private final LocationMapper locationMapper;

    @Operation(summary = "위치 입력", description = "Create a new location with the provided details.")
    @PostMapping("/locations")
    public ResponseEntity<ApiResponseDto<CentroidResponse>> createLocation(
        @Valid @RequestBody LocationRequest locationRequest) {
        createLocationUsecase.createLocation(
            locationRequest.roomId(),
            locationRequest.memberId(),
            locationRequest.latitude(),
            locationRequest.longitude()
        );

        Pair<BigDecimal, BigDecimal> centroid = loadCentroidUsecase.readCentroidCoordinates(
            locationRequest.roomId());

        updateRoomUseCase.updateRoomVoteDeadline(locationRequest.roomId(), LocalDateTime.now());

        return ResponseEntity.ok(ApiResponseDto.success(
            CentroidResponse.of(locationRequest.roomId(), centroid.getFirst(), centroid.getSecond())
        ));
    }

    // For Test
    @Operation(summary = "중심 좌표 조회", description = "Retrieve the centroid coordinates for a location using its roomId")
    @GetMapping("/locations/centroid/{roomId}")
    public ResponseEntity<ApiResponseDto<CentroidResponse>> getCentroid(
        @PathVariable String roomId) {
        Pair<BigDecimal, BigDecimal> centroid = loadCentroidUsecase.readCentroidCoordinates(roomId);

        return ResponseEntity.ok(ApiResponseDto.success(
            CentroidResponse.of(roomId, centroid.getFirst(), centroid.getSecond())
        ));
    }

    @Operation(summary = "위치 조회 Basic", description = "Retrieve detailed information for a location using its roomId and member ID")
    @GetMapping("/locations/{roomId}/{memberId}")
    public ResponseEntity<ApiResponseDto<LocationResponse>> getLocation(@PathVariable String roomId,
        @PathVariable String memberId) {
        Location location = readLocationUsecase.readLocation(roomId, memberId);
        Member member = getRoomUseCase.getParticipant(roomId, memberId);

        return ResponseEntity.ok(ApiResponseDto.success(
            locationMapper.toResponse(location, member)
        ));
    }

    @Operation(summary = "위치조회 ConvexHull", description = "Retrieve the ConvexHull inside list, outside list of locations for a roomId")
    @GetMapping("/locations/ConvH/{roomId}")
    public ResponseEntity<ApiResponseDto<ConvexHullLocationResponse>> getConvexHullLocations(
        @PathVariable String roomId) {
        List<LocationResponse> convexHull = locationMapper.toResponseList(
            readLocationUsecase.readConvexHull(roomId));
        List<LocationResponse> inside = locationMapper.toResponseList(
            readLocationUsecase.readInsideConvexHull(roomId));

        return ResponseEntity.ok(
            ApiResponseDto.success(ConvexHullLocationResponse.of(convexHull, inside)));
    }

    @Operation(summary = "위치 목록 조회", description = "Retrieve the list of locations for a roomId")
    @GetMapping("/locations/{roomId}")
    public ResponseEntity<ApiResponseDto<List<LocationResponse>>> getLocations(
        @PathVariable String roomId) {
        List<LocationResponse> responses = locationMapper.toResponseList(
            readLocationUsecase.readLocations(roomId));

        return ResponseEntity.ok(ApiResponseDto.success(responses));
    }

    @Operation(summary = "위치 수정", description = "Update the location with the provided details.")
    @PutMapping("/locations")
    public ResponseEntity<ApiResponseDto<LocationResponse>> updateLocation(
        @Valid @RequestBody LocationRequest locationRequest) {
        Location location = createLocationUsecase.updateLocation(
            locationRequest.roomId(),
            locationRequest.memberId(),
            locationRequest.latitude(),
            locationRequest.longitude()
        );
        LocationResponse response = locationMapper.toResponse(location);

        return ResponseEntity.ok(ApiResponseDto.success(response));
    }
}

