package com.kok.kokapi.room.adapter.in.dto.request;


import jakarta.validation.constraints.NotBlank;

public record JoinRoomParticipantRequest(
        @NotBlank(message = "프로필 정보는 필수입니다.")
        String profile
) {}

