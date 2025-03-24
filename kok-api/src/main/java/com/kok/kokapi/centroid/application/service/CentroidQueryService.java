package com.kok.kokapi.centroid.application.service;

import com.kok.kokapi.config.geometry.PointConverter;
import com.kok.kokcore.location.port.out.ReadCentroidPort;
import com.kok.kokcore.location.usecase.LoadCentroidUseCase;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CentroidQueryService implements LoadCentroidUseCase {

    private final ReadCentroidPort readCentroidPort;
    private final PointConverter pointConverter;

    @Override
    public Point readCentroid(String roomId) {
        return readCentroidPort.findCentroidByRoomId(roomId);
    }

    @Override
    public Pair<BigDecimal, BigDecimal> readCentroidCoordinates(String roomId) {
        Point centroidPoint = readCentroidPort.findCentroidByRoomId(roomId);
        if (centroidPoint == null) {
            throw new IllegalArgumentException("해당 roomId에 대한 중심점을 찾을 수 없습니다.");
        }
        return pointConverter.toCoordinates(centroidPoint);
    }
}
