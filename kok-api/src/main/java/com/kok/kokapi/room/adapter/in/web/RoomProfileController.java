package com.kok.kokapi.room.adapter.in.web;

import com.kok.kokapi.common.response.ApiResponseDto;
import com.kok.kokapi.config.annotion.V1Controller;
import com.kok.kokapi.room.adapter.in.dto.response.RandomProfileResponse;
import com.kok.kokcore.room.domain.Profile;
import com.kok.kokcore.room.usecase.CreateRandomProfileUseCase;
import com.kok.kokcore.room.usecase.GetMemberProfileUseCase;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@V1Controller
@RequiredArgsConstructor
public class RoomProfileController {

    private final CreateRandomProfileUseCase createRandomProfileUseCase;
    private final GetMemberProfileUseCase getMemberProfileUseCase;

    @Operation(summary = "랜덤 프로필 및 닉네임 조회", description = "랜덤으로 생성한 프로필 이미지와 닉네임을 반환합니다.")
    @GetMapping("/rooms/profile/random")
    public ResponseEntity<ApiResponseDto<RandomProfileResponse>> getRandomProfile() {
        Profile profile = createRandomProfileUseCase.createProfile();
        RandomProfileResponse response = new RandomProfileResponse(
                profile.getImageUrl(),
                profile.getNickname()
        );

        return ResponseEntity.ok(ApiResponseDto.success(response));
    }

    @Operation(summary = "약속방 참여자의 프로필 조회", description = "약속방 참여자가 기존에 생성한 프로필 목록을 조회하며, 없을 경우 빈 값으로 반환합니다.")
    @GetMapping("/rooms/{roomId}/profiles")
    public ResponseEntity<ApiResponseDto<List<RandomProfileResponse>>> getMemberProfiles(@PathVariable String roomId) {
        List<Profile> profiles = getMemberProfileUseCase.getProfilesByRoomId(roomId);
        List<RandomProfileResponse> responseList = profiles.stream()
                .map(profile -> new RandomProfileResponse(profile.getImageUrl(), profile.getNickname()))
                .toList();

        return ResponseEntity.ok(ApiResponseDto.success(responseList));
    }
}
