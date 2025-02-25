package com.kok.kokcore.application.port.out;

import org.locationtech.jts.geom.Point;

public interface ReadCentroidPort {
    Point findCentroidByUuid(String uuid);
}
