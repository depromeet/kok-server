package com.kok.kokapi.centroid.adapter.inbound.dto;

import java.math.BigDecimal;

public record LocationRequest(
        String uuid,
        Integer memberId,
        BigDecimal latitude,
        BigDecimal longitude
) {
}
