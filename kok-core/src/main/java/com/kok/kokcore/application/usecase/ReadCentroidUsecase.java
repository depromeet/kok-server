package com.kok.kokcore.application.usecase;

import org.locationtech.jts.geom.Point;

public interface ReadCentroidUsecase {
    Point readCentroid(String uuid);
}
