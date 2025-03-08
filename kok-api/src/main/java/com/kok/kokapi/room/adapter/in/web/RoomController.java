package com.kok.kokapi.room.adapter.in.web;

import com.kok.kokapi.common.response.ApiResponseDto;
import com.kok.kokapi.config.annotion.V1Controller;
import com.kok.kokapi.room.adapter.in.dto.request.CreateRoomRequest;
import com.kok.kokapi.room.adapter.in.dto.request.JoinRoomParticipantRequest;
import com.kok.kokapi.room.adapter.in.dto.response.JoinRoomResponse;
import com.kok.kokapi.room.adapter.in.dto.response.RoomCreateResponse;
import com.kok.kokapi.room.adapter.in.dto.response.RoomMembersResponse;
import com.kok.kokapi.room.adapter.in.dto.response.RoomDetailResponse;
import com.kok.kokcore.room.domain.Member;
import com.kok.kokcore.room.domain.vo.MemberRole;
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

@V1Controller
@RequiredArgsConstructor
public class RoomController {

    private final GetRoomUseCase getRoomUseCase;
    private final CreateRoomUseCase createRoomUseCase;
    private final JoinRoomUseCase joinRoomUseCase;

    @Operation(summary = "약속방 조회", description = "약속방 ID를 통해 약속방을 조회합니다.")
    @GetMapping("/rooms/{roomId}")
    public ResponseEntity<ApiResponseDto<RoomDetailResponse>> getRoomDetail(@PathVariable String roomId) {
        var room = getRoomUseCase.findRoomById(roomId);
        var response = RoomDetailResponse.from(room);
        return ResponseEntity.ok(ApiResponseDto.success(response));
    }

    @Operation(summary = "약속방 생성", description = "새로운 약속방을 생성합니다.")
    @PostMapping("/rooms")
    public ResponseEntity<ApiResponseDto<RoomCreateResponse>> createRoom(@Valid @RequestBody CreateRoomRequest request) {
        var host = new Member(request.hostNickname(), request.hostProfile(), MemberRole.LEADER);
        var room = createRoomUseCase.createRoom(
                request.roomName(),
                request.capacity(),
                host
        );

        var response = RoomCreateResponse.from(room, 1, 1);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDto.success(response));
    }

    @Operation(summary = "약속방 참여자 프로필 목록 조회", description = "약속방에 참여 중인 참여자들의 프로필 목록을 반환합니다.")
    @GetMapping("/rooms/{roomId}/participants")
    public ResponseEntity<ApiResponseDto<List<RoomMembersResponse>>> getParticipants(@PathVariable String roomId) {
        var room = getRoomUseCase.findRoomById(roomId);
        var participants = getRoomUseCase.getParticipants(room.getId());

        var response = participants.stream()
                .map(member -> new RoomMembersResponse(
                        member.getProfile(),
                        member.getNickname(),
                        member.getRole()
                )).toList();
        return ResponseEntity.ok(ApiResponseDto.success(response));
    }

    @Operation(summary = "약속방 참여", description = "사용자가 약속방에 참여합니다.")
    @PostMapping("/rooms/{roomId}/join")
    public ResponseEntity<ApiResponseDto<JoinRoomResponse>> joinRoom(@PathVariable String roomId,
                                                                     @Valid @RequestBody JoinRoomParticipantRequest request) {

        getRoomUseCase.findRoomById(roomId);
        var participant = new Member(request.nickname(), request.profile(), MemberRole.FOLLOWER);
        var participantCount = joinRoomUseCase.joinRoom(roomId, participant);
        var memberId = participantCount;
        JoinRoomResponse response = new JoinRoomResponse(participantCount, memberId);
        return ResponseEntity.ok(ApiResponseDto.success(response));
    }
}
