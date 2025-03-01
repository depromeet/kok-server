package com.kok.kokapi.centroid.adapter.out.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record CentroidResponse(
        String uuid,
        BigDecimal latitude,
        BigDecimal longitude
) {
    public static CentroidResponse of(String uuid, BigDecimal latitude, BigDecimal longitude) {
        return new CentroidResponse(uuid,
                latitude.setScale(6, RoundingMode.HALF_UP),
                longitude.setScale(6,RoundingMode.HALF_UP));
    }
}
