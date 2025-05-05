package com.kok.kokcore.station.port.out;

import com.kok.kokcore.station.domain.entity.Station;
import java.util.List;
import java.util.Optional;
import org.locationtech.jts.geom.Point;

public interface RetrieveStationsPort {

    Optional<Station> retrieveStation(Long stationId);

    List<Station> retrieveInRangeStations(Point centroid, double dist);

    List<Station> retrieveStationsByKeyword(String keyword);
}
