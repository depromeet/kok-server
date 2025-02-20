package com.kok.kokapi.config.geometry;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.locationtech.jts.geom.Point;
import org.springframework.data.util.Pair;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class GeometryConfig {

    @Bean
    public GeometryFactory geometryFactory() {
        return new GeometryFactory();
    }

    @Bean
    public PointConverter pointConverter(GeometryFactory geometryFactory) {
        return new PointConverter(geometryFactory);
    }

    // Converter 클래스
    public static class PointConverter {

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

}
