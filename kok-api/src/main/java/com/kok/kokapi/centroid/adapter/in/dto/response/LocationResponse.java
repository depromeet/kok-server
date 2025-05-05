package com.kok.kokapi.centroid.adapter.in.dto.response;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record LocationResponse(
    String roomId,
    String memberId,
    String imageUrl,
    BigDecimal latitude,
    BigDecimal longitude,
    String name
) {

    public static LocationResponse of(String roomId, String memberId, BigDecimal latitude,
        BigDecimal longitude, String name) {
        return new LocationResponse(roomId, memberId, "",
            latitude.setScale(6, RoundingMode.HALF_UP),
            longitude.setScale(6, RoundingMode.HALF_UP),
            name);
    }

    public static LocationResponse of(String roomId, String memberId, String imageUrl, BigDecimal latitude,
        BigDecimal longitude, String name) {
        return new LocationResponse(roomId, memberId, imageUrl,
            latitude.setScale(6, RoundingMode.HALF_UP),
            longitude.setScale(6, RoundingMode.HALF_UP),
            name);
    }
}
