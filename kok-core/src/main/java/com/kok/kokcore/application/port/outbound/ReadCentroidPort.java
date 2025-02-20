package com.kok.kokcore.application.port.outbound;

import com.kok.kokcore.application.domain.entity.Location;
import org.locationtech.jts.geom.Point;
import java.util.List;

public interface ReadCentroidPort {
    Point findCentroidByUuid(String uuid);
}
