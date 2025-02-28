package com.kok.kokapi.centroid.application.service;

import com.kok.kokapi.config.geometry.PointConverter;
import com.kok.kokcore.application.port.out.ReadCentroidPort;
import com.kok.kokcore.application.usecase.ReadCentroidUsecase;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CentroidQueryService implements ReadCentroidUsecase {

    private final ReadCentroidPort readCentroidPort;
    private final PointConverter pointConverter;

    @Override
    public Point readCentroid(String uuid) {
        return readCentroidPort.findCentroidByUuid(uuid);
    }

    @Override
    public Pair<BigDecimal, BigDecimal> readCentroidCoordinates(String uuid) {
        Point centroidPoint = readCentroidPort.findCentroidByUuid(uuid);
        if (centroidPoint == null) {
            throw new IllegalArgumentException("해당 UUID에 대한 중심점을 찾을 수 없습니다.");
        }
        return pointConverter.toCoordinates(centroidPoint);
    }
}
