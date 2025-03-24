package com.kok.kokapi.fixture;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

public class PointFixture {

    public static Point create(){
        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate coordinate = new Coordinate(127.02758, 37.49794);

        return geometryFactory.createPoint(coordinate);
    }
}
