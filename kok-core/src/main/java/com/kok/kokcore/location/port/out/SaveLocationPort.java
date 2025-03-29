package com.kok.kokcore.location.port.out;

import com.kok.kokcore.location.domain.Location;
import org.locationtech.jts.geom.Point;

public interface SaveLocationPort {

    Location saveLocation(String roomId, String memberId, Point point, String name);
}
