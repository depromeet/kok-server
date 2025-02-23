package com.kok.kokapi.room.adapter.in.web;

import com.kok.kokapi.common.response.ApiResponseDto;
import com.kok.kokapi.room.adapter.in.dto.request.CreateRoomRequest;
import com.kok.kokapi.room.adapter.in.dto.response.RoomResponse;
import com.kok.kokcore.domain.Room;
import com.kok.kokcore.usecase.CreateRoomUseCase;
import com.kok.kokcore.usecase.GetRoomUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final CreateRoomUseCase createRoomUseCase;
    private final GetRoomUseCase getRoomUseCase;

    @GetMapping("{roomId}")
    public ResponseEntity<ApiResponseDto<RoomResponse>> getRoomDetail(@PathVariable String roomId) {
        Room room = getRoomUseCase.getRoomById(roomId);
        var response = RoomResponse.from(room);
        return ResponseEntity.ok(ApiResponseDto.success(response));
    }

    @PostMapping("/create")
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
