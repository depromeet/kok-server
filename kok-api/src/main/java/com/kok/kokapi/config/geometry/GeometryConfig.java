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
}
