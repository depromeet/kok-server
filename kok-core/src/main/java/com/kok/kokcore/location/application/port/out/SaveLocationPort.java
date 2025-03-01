package com.kok.kokcore.location.application.port.out;

import com.kok.kokcore.location.domain.Location;
import org.locationtech.jts.geom.Point;

public interface SaveLocationPort {
    Location saveLocation(String uuid, Integer memberId, Point point);
}
