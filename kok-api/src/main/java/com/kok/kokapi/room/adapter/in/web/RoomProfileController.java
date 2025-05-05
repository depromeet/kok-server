package com.kok.kokapi.room.adapter.in.web;

import com.kok.kokapi.common.response.ApiResponseDto;
import com.kok.kokapi.config.annotion.V1Controller;
import com.kok.kokapi.room.adapter.in.dto.response.RandomProfileResponse;
import com.kok.kokcore.room.domain.Profile;
import com.kok.kokcore.room.usecase.CreateRandomProfileUseCase;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@V1Controller
@RequiredArgsConstructor
public class RoomProfileController {

    private final CreateRandomProfileUseCase createRandomProfileUseCase;

    @Operation(summary = "랜덤 프로필 및 닉네임 생성 및 조회", description = "랜덤으로 생성한 프로필 이미지와 닉네임을 반환합니다.")
    @GetMapping("/rooms/profile/random")
    public ResponseEntity<ApiResponseDto<RandomProfileResponse>> getRandomProfile() {
        Profile profile = createRandomProfileUseCase.createProfile();
        RandomProfileResponse response = new RandomProfileResponse(
            profile.getImageUrl(),
            profile.getNickname()
        );

        return ResponseEntity.ok(ApiResponseDto.success(response));
    }
}
