package com.kok.kokcore.application.port.outbound;

import com.kok.kokcore.application.domain.entity.Location;
import org.locationtech.jts.geom.Point;

public interface SaveLocationPort {
    Location saveLocation(String uuid, Integer memberId, Point point);
}
