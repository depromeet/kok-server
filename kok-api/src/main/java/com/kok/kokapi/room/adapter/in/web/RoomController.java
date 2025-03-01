package com.kok.kokapi.room.adapter.in.web;

import com.kok.kokapi.common.adapter.in.web.BaseController;
import com.kok.kokapi.common.response.ApiResponseDto;
import com.kok.kokapi.room.adapter.in.dto.request.CreateRoomRequest;
import com.kok.kokapi.room.adapter.in.dto.request.JoinRoomParticipantRequest;
import com.kok.kokapi.room.adapter.in.dto.response.RoomMembersResponse;
import com.kok.kokapi.room.adapter.in.dto.response.RoomDetailResponse;
import com.kok.kokcore.room.domain.Member;
import com.kok.kokcore.room.domain.Room;
import com.kok.kokcore.room.usecase.CreateRoomUseCase;
import com.kok.kokcore.room.usecase.GetRoomUseCase;
import com.kok.kokcore.room.usecase.JoinRoomUseCase;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RoomController extends BaseController {

    private final GetRoomUseCase getRoomUseCase;
    private final CreateRoomUseCase createRoomUseCase;
    private final JoinRoomUseCase joinRoomUseCase;

    @Operation(summary = "약속방 조회", description = "Retrieve detailed information for a room using its ID")
    @GetMapping("/rooms/{roomId}")
    public ResponseEntity<ApiResponseDto<RoomDetailResponse>> getRoomDetail(@PathVariable String roomId) {
        Room room = getRoomUseCase.findRoomById(roomId);
        var response = RoomDetailResponse.from(room);
        return ResponseEntity.ok(ApiResponseDto.success(response));
    }

    @Operation(summary = "약속방 생성", description = "Create a new room with the provided details.")
    @PostMapping("/rooms")
    public ResponseEntity<ApiResponseDto<RoomDetailResponse>> createRoom(@Valid @RequestBody CreateRoomRequest request) {
        Member host = new Member(request.hostNickname(), request.hostProfile(), "Leader");
        Room room = createRoomUseCase.createRoom(
                request.roomName(),
                request.capacity(),
                host,
                request.password()
        );

        var response = RoomDetailResponse.from(room);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDto.success(response));
    }

    @Operation(summary = "약속방 참여자 프로필 목록 조회", description = "Retrieve the list of participant profiles for the room.")
    @GetMapping("/rooms/{roomId}/participants")
    public ResponseEntity<ApiResponseDto<List<RoomMembersResponse>>> getParticipants(@PathVariable String roomId) {
        Room room = getRoomUseCase.findRoomById(roomId);
        List<Member> participants = getRoomUseCase.getParticipants(room.getId());

        var response = participants.stream()
                .map(member -> new RoomMembersResponse(
                        member.getProfile(),
                        member.getNickname(),
                        member.getRole()
                )).toList();
        return ResponseEntity.ok(ApiResponseDto.success(response));
    }

    @Operation(summary = "약속방 참여", description = "Register a new participant profile for the room.")
    @PostMapping("/rooms/{roomId}/join")
    public ResponseEntity<ApiResponseDto<String>> joinRoom(@PathVariable String roomId,
                                                           @Valid @RequestBody JoinRoomParticipantRequest request) {
        getRoomUseCase.findRoomById(roomId);
        Member participant = new Member(request.nickname(), request.profile(), "Follower");
        joinRoomUseCase.joinRoom(roomId, participant);
        return ResponseEntity.ok(ApiResponseDto.success("약속방 참여가 완료되었습니다."));
    }
}
