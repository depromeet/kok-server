package com.kok.kokapi.config.geometry;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.data.util.Pair;

import java.math.BigDecimal;

public class PointConverter {

    private final GeometryFactory geometryFactory;

    public PointConverter(GeometryFactory geometryFactory) {
        this.geometryFactory = geometryFactory;
    }

    public Point fromCoordinates(BigDecimal latitude, BigDecimal longitude) {
        Coordinate coordinate = new Coordinate(longitude.doubleValue(), latitude.doubleValue());
        Point point = geometryFactory.createPoint(coordinate);
        point.setSRID(4326); // WGS84 좌표계 -> Kakao, Naver 등에서 사용하는 좌표계
        return point;
    }

    public Pair<BigDecimal,BigDecimal> toCoordinates(Point point) {
        return Pair.of(BigDecimal.valueOf(point.getY()), BigDecimal.valueOf(point.getX()));
    }

}
