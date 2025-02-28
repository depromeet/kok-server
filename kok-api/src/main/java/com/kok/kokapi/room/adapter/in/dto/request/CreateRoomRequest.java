package com.kok.kokapi.room.adapter.in.dto.request;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateRoomRequest(
        @NotBlank(message = "방 이름은 필수 입력값입니다.")
        @Size(max = 30, message = "방 이름은 최대 30자까지 가능합니다.")
        String roomName,

        @Min(value = 2, message = "참여 인원 수는 최소 2명 이상이어야 합니다.")
        Integer capacity,

        String hostProfile,

        @NotBlank(message = "방장 이름(닉네임)은 필수 입력값입니다.")
        String hostNickname,

        @NotBlank(message = "비밀번호는 필수 입력값입니다.")
        String password
) {}
