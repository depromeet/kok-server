package com.kok.kokcore.application.usecase;

import org.locationtech.jts.geom.Point;
import org.springframework.data.util.Pair;

import java.math.BigDecimal;

public interface ReadCentroidUsecase {
    Point readCentroid(String uuid);

    Pair<BigDecimal, BigDecimal> readCentroidCoordinates(String uuid);
}
