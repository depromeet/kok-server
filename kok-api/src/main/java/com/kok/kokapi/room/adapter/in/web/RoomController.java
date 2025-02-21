package com.kok.kokapi.room.adapter.in.web;

import com.kok.kokapi.common.response.ApiResponseDto;
import com.kok.kokapi.room.adapter.in.dto.request.CreateRoomRequest;
import com.kok.kokapi.room.adapter.in.dto.response.RoomResponse;
import com.kok.kokapi.room.application.service.RoomService;
import com.kok.kokcore.domain.Room;
import com.kok.kokcore.usecase.CreateRoomUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final CreateRoomUseCase createRoomUseCase;
    private final RoomService roomService;

    /**
     * 약속방 정보 및 참여자 목록 조회
     * @param roomId
     * @return
     */
    @GetMapping("{roomId}")
    public ResponseEntity<ApiResponseDto<RoomDetailResponse>> getRoomDetail(@PathVariable String roomId) {
        Room room = roomService.getRoomById(roomId);

    }

    /**
     * 약속방 생성
     * @param request
     * @return
     */
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
