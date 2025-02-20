package com.kok.kokapi.room.adapter.in.web;

import com.kok.kokapi.common.response.ApiResponseDto;
import com.kok.kokapi.room.adapter.in.dto.request.CreateRoomRequest;
import com.kok.kokapi.room.adapter.in.dto.response.RoomResponse;
import com.kok.kokcore.domain.Room;
import com.kok.kokcore.usecase.CreateRoomUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final CreateRoomUseCase createRoomUseCase;

    @PostMapping("/create")
    public ResponseEntity<ApiResponseDto<RoomResponse>> createRoom(@Valid @RequestBody CreateRoomRequest request) {
        Room room = createRoomUseCase.createRoom(
                request.getRoomName(),
                request.getCapacity(),
                request.getHostProfile(),
                request.getPassword()
        );

        var response = RoomResponse.from(room);
        return ResponseEntity.ok(ApiResponseDto.success(response));
    }
}
