package com.kok.kokapi.room.adapter.in.web;

import com.kok.kokapi.common.response.ApiResponseDto;
import com.kok.kokapi.config.annotion.V1Controller;
import com.kok.kokapi.room.adapter.in.dto.request.CreateRoomRequest;
import com.kok.kokapi.room.adapter.in.dto.request.JoinRoomParticipantRequest;
import com.kok.kokapi.room.adapter.in.dto.response.CreateRoomResponse;
import com.kok.kokapi.room.adapter.in.dto.response.JoinRoomResponse;
import com.kok.kokapi.room.adapter.in.dto.response.RoomDetailResponse;
import com.kok.kokapi.room.adapter.in.dto.response.RoomParticipantsResponse;
import com.kok.kokapi.room.adapter.in.dto.response.RoomStatusResponse;
import com.kok.kokcore.location.domain.Location;
import com.kok.kokcore.location.usecase.ReadLocationUseCase;
import com.kok.kokcore.room.domain.Member;
import com.kok.kokcore.room.domain.Room;
import com.kok.kokcore.room.domain.vo.MemberRole;
import com.kok.kokcore.room.usecase.CreateRoomUseCase;
import com.kok.kokcore.room.usecase.GetRoomUseCase;
import com.kok.kokcore.room.usecase.JoinRoomUseCase;
import com.kok.kokcore.room.usecase.UpdateRoomUseCase;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@V1Controller
@RequiredArgsConstructor
public class RoomController {

    private final GetRoomUseCase getRoomUseCase;
    private final CreateRoomUseCase createRoomUseCase;
    private final JoinRoomUseCase joinRoomUseCase;
    private final ReadLocationUseCase readLocationUseCase;
    private final UpdateRoomUseCase updateRoomUseCase;

    @Operation(summary = "약속방 조회", description = "약속방 ID를 통해 약속방을 조회합니다. 투표 모드에 대한 값을 포함합니다.")
    @GetMapping("/rooms/{roomId}")
    public ResponseEntity<ApiResponseDto<RoomDetailResponse>> getRoomDetail(
        @PathVariable String roomId) {
        Room room = updateRoomUseCase.updateRoomStatus(roomId, LocalDateTime.now());
        long participantsCount = getRoomUseCase.getParticipantsCount(roomId);
        RoomDetailResponse response = RoomDetailResponse.of(room, participantsCount);
        return ResponseEntity.ok(ApiResponseDto.success(response));
    }

    @Operation(summary = "약속방 상태 조회", description = "약속방 ID를 통해 현재 약속방 상태(LOCATION_INPUT/VOTE/VOTE_RESULT)를 반환합니다.")
    @GetMapping("/rooms/{roomId}/status")
    public ResponseEntity<ApiResponseDto<RoomStatusResponse>> getRoomStatus(
        @PathVariable String roomId) {
        Room room = updateRoomUseCase.updateRoomStatus(roomId, LocalDateTime.now());
        RoomStatusResponse response = RoomStatusResponse.of(room);
        return ResponseEntity.ok(ApiResponseDto.success(response));
    }

    @Operation(summary = "약속방 생성", description = "새로운 약속방을 생성합니다.")
    @PostMapping("/rooms")
    public ResponseEntity<ApiResponseDto<CreateRoomResponse>> createRoom(
        @Valid @RequestBody CreateRoomRequest request) {
        String nickname = request.hostNickname();
        String profile = request.hostProfile();
        Member host = new Member(nickname, profile, MemberRole.LEADER);

        Room room = createRoomUseCase.createRoom(
            request.roomName(),
            request.capacity(),
            host
        );

        CreateRoomResponse response = CreateRoomResponse.of(room, 1, room.getCapacity() - 1);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponseDto.success(response));
    }

    @Operation(summary = "약속방 참여자 프로필 목록 조회", description = "약속방에 참여 중인 참여자들의 프로필 목록을 반환합니다.")
    @GetMapping("/rooms/{roomId}/participants")
    public ResponseEntity<ApiResponseDto<RoomParticipantsResponse>> getParticipants(
        @PathVariable String roomId) {
        Room room = updateRoomUseCase.updateRoomStatus(roomId, LocalDateTime.now());
        List<Member> participants = getRoomUseCase.getParticipants(room.getId());
        List<Location> locations = readLocationUseCase.readLocations(room.getId());

        RoomParticipantsResponse responses = RoomParticipantsResponse.of(room, participants,
            locations);
        return ResponseEntity.ok(ApiResponseDto.success(responses));
    }

    @Operation(summary = "약속방 참여", description = "사용자가 약속방에 참여합니다.")
    @PostMapping("/rooms/{roomId}/join")
    public ResponseEntity<ApiResponseDto<JoinRoomResponse>> joinRoom(@PathVariable String roomId,
        @Valid @RequestBody JoinRoomParticipantRequest request) {
        Room room = updateRoomUseCase.updateRoomStatus(roomId, LocalDateTime.now());

        Member participant = new Member(request.nickname(), request.profile(), MemberRole.FOLLOWER);
        int participantCount = joinRoomUseCase.joinRoom(roomId, participant);
        int nonParticipantCount = room.getCapacity() - participantCount;

        JoinRoomResponse response = new JoinRoomResponse(
            participant.getMemberId(),
            participant.getProfile(),
            participant.getNickname(),
            participantCount,
            nonParticipantCount
        );

        return ResponseEntity.ok(ApiResponseDto.success(response));
    }
}
