package com.kok.kokcore.location.port.out;

import org.locationtech.jts.geom.Point;

public interface ReadCentroidPort {

    Point findCentroidByRoomId(String roomId);
}
