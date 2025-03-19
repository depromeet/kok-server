package com.kok.kokcore.location.usecase;

import org.locationtech.jts.geom.Point;
import org.springframework.data.util.Pair;

import java.math.BigDecimal;

public interface LoadCentroidUseCase {
    Point readCentroid(String uuid);

    Pair<BigDecimal, BigDecimal> readCentroidCoordinates(String uuid);
}
