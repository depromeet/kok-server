package com.kok.kokapi.centroid.adapter.out.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record LocationResponse (
    String uuid,
    Integer memberId,
    BigDecimal latitude,
    BigDecimal longitude
){
    public static LocationResponse of(String uuid, Integer memberId, BigDecimal latitude, BigDecimal longitude) {
        return new LocationResponse(uuid, memberId,
                latitude.setScale(6, RoundingMode.HALF_UP),
                longitude.setScale(6, RoundingMode.HALF_UP));
    }
}
