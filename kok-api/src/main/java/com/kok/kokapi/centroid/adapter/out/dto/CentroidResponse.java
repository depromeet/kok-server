package com.kok.kokapi.centroid.adapter.out.dto;

import java.math.BigDecimal;

public record CentroidResponse(
        String uuid,
        BigDecimal latitude,
        BigDecimal longitude
) {
    public static CentroidResponse of(String uuid, BigDecimal latitude, BigDecimal longitude) {
        return new CentroidResponse(uuid, latitude, longitude);
    }
}
