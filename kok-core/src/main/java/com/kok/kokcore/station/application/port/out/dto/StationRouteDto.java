package com.kok.kokcore.station.application.port.out.dto;

import com.kok.kokcore.station.domain.entity.Route;
import com.kok.kokcore.station.domain.entity.Station;

public record StationRouteDto(
    String name,
    String latitude,
    String longitude,
    Long stationId,
    String route
) {

    public boolean hasName(Station station) {
        return name.equals(station.getName());
    }

    public Station toStation() {
        return new Station(name, longitude, longitude);
    }

    public Route toRouteByStation(Station station) {
        return new Route(stationId, route, station);
    }
}
