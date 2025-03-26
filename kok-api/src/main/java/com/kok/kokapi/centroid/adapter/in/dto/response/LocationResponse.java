package com.kok.kokapi.centroid.adapter.in.dto.response;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record LocationResponse(
    String roomId,
    String memberId,
    BigDecimal latitude,
    BigDecimal longitude
) {

    public static LocationResponse of(String roomId, String memberId, BigDecimal latitude,
        BigDecimal longitude) {
        return new LocationResponse(roomId, memberId,
            latitude.setScale(6, RoundingMode.HALF_UP),
            longitude.setScale(6, RoundingMode.HALF_UP));
    }
}
