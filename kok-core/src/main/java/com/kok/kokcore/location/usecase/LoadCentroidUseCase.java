package com.kok.kokcore.location.usecase;

import java.math.BigDecimal;
import org.locationtech.jts.geom.Point;
import org.springframework.data.util.Pair;

public interface LoadCentroidUseCase {

    Point readCentroid(String roomId);

    Pair<BigDecimal, BigDecimal> readCentroidCoordinates(String roomId);
}