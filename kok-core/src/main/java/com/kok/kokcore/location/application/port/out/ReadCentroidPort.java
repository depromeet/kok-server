package com.kok.kokcore.location.application.port.out;

import org.locationtech.jts.geom.Point;

public interface ReadCentroidPort {
    Point findCentroidByUuid(String uuid);
}
