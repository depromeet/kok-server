package com.kok.kokapi.public_transportation.adapter.in.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RouteRequest(
        @NotBlank(message = "UUID는 필수입니다.")
        String UUID,
        @NotNull(message = "Member ID(멤버 일련번호)는 필수입니다.")
        Integer memberId
) {
}
