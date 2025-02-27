package com.kok.kokcore.application.usecase;

import com.kok.kokcore.application.domain.entity.Location;
import org.locationtech.jts.geom.Point;

import java.math.BigDecimal;


public interface CreateLocationUsecase {
    Location createLocation(String uuid, Integer memberId, BigDecimal latitude, BigDecimal longitude);
    Location updateLocation(String uuid, Integer memberId, BigDecimal latitude, BigDecimal longitude);
}
