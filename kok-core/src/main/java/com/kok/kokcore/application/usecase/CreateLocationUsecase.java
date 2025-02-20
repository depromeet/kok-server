package com.kok.kokcore.application.usecase;

import com.kok.kokcore.application.domain.entity.Location;
import org.locationtech.jts.geom.Point;


public interface CreateLocationUsecase {
    Location createLocation(String uuid, Integer memberId, Point point);
    Location UpdateLocation(String uuid, Integer memberId, Point point);
}
