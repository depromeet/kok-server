package com.kok.kokapi.centroid.adapter.in.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record LocationRequest(

        @NotBlank(message = "uuid는 필수입니다.")
        String uuid,

        @NotNull(message = "memberId(멤버 일련번호)는 필수입니다.")
        Integer memberId,

        @NotNull(message = "latitude(위도)는 필수입니다.")
        @DecimalMin(value = "33.0", message = "위도는 33.0 이상이어야 합니다.")
        @DecimalMax(value = "43.0", message = "위도는 43.0 이하여야 합니다.")
        BigDecimal latitude,

        @NotNull(message = "longitude(경도)는 필수입니다.")
        @DecimalMin(value = "124.0", message = "경도는 124.0 이상이어야 합니다.")
        @DecimalMax(value = "132.0", message = "경도는 132.0 이하여야 합니다.")
        BigDecimal longitude
) {
}
