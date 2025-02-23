package com.kok.kokapi.room.adapter.in.web;

import com.kok.kokapi.adapter.in.web.BaseController;
import com.kok.kokapi.common.response.ApiResponseDto;
import com.kok.kokapi.room.adapter.in.dto.request.CreateRoomRequest;
import com.kok.kokapi.room.adapter.in.dto.response.RoomResponse;
import com.kok.kokcore.room.domain.Room;
import com.kok.kokcore.room.usecase.CreateRoomUseCase;
import com.kok.kokcore.room.usecase.GetRoomUseCase;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class RoomController extends BaseController {

    private final CreateRoomUseCase createRoomUseCase;
    private final GetRoomUseCase getRoomUseCase;

    @Operation(summary = "약속방 조회", description = "Retrieve detailed information for a room using its ID")
    @GetMapping("/rooms/{roomId}")
    public ResponseEntity<ApiResponseDto<RoomResponse>> getRoomDetail(@PathVariable String roomId) {
        Room room = getRoomUseCase.getRoomById(roomId);
        var response = RoomResponse.from(room);
        return ResponseEntity.ok(ApiResponseDto.success(response));
    }

    @Operation(summary = "약속방 생성", description = "Create a new room with the provided details.")
    @PostMapping("/rooms/create")
    public ResponseEntity<ApiResponseDto<RoomResponse>> createRoom(@Valid @RequestBody CreateRoomRequest request) {
        Room room = createRoomUseCase.createRoom(
                request.roomName(),
                request.capacity(),
                request.hostProfile(),
                request.password()
        );

        var response = RoomResponse.from(room);
        return ResponseEntity.ok(ApiResponseDto.success(response));
    }

}
