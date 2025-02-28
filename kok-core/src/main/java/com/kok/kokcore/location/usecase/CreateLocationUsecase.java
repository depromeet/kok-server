package com.kok.kokcore.location.usecase;

import com.kok.kokcore.location.domain.Location;

import java.math.BigDecimal;


public interface CreateLocationUsecase {
    Location createLocation(String uuid, Integer memberId, BigDecimal latitude, BigDecimal longitude);
    Location updateLocation(String uuid, Integer memberId, BigDecimal latitude, BigDecimal longitude);
}
