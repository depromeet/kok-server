package com.kok.kokcore.location.usecase;

import java.math.BigDecimal;
import org.locationtech.jts.geom.Point;
import org.springframework.data.util.Pair;

public interface LoadCentroidUseCase {

<<<<<<<< HEAD:kok-core/src/main/java/com/kok/kokcore/location/usecase/LoadCentroidUseCase.java
    Point readCentroid(String roomId);
========
public interface ReadCentroidUseCase {
    Point readCentroid(String uuid);
>>>>>>>> 5c76cf9 (:sparkles: feat: add station recommend):kok-core/src/main/java/com/kok/kokcore/location/usecase/ReadCentroidUseCase.java

    Pair<BigDecimal, BigDecimal> readCentroidCoordinates(String roomId);
}
