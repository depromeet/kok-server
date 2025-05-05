package com.kok.kokapi.centroid.adapter.in.dto.response;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record CentroidResponse(
    String roomId,
    BigDecimal latitude,
    BigDecimal longitude
) {

    public static CentroidResponse of(String roomId, BigDecimal longitude, BigDecimal latitude) {
        return new CentroidResponse(roomId,
            latitude.setScale(6, RoundingMode.HALF_UP),
            longitude.setScale(6, RoundingMode.HALF_UP));
    }
}
