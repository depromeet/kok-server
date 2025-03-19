package com.kok.kokapi.public_transportation.adapter.in.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RouteRequest(
        @NotBlank(message = "roomId는 필수입니다.")
        String roomId,
        @NotNull(message = "Member ID(멤버 일련번호)는 필수입니다.")
        String memberId
) {
}
