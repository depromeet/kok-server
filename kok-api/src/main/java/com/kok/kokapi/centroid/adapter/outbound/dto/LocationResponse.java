package com.kok.kokapi.centroid.adapter.outbound.dto;

import java.math.BigDecimal;

public record LocationResponse (
    String uuid,
    Integer memberId,
    BigDecimal latitude,
    BigDecimal longitude
){
    public static LocationResponse of(String uuid, Integer memberId, BigDecimal latitude, BigDecimal longitude) {
        return new LocationResponse(uuid, memberId, latitude, longitude);
    }
}
