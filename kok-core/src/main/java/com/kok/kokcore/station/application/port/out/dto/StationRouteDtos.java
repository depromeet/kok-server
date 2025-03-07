package com.kok.kokcore.station.application.port.out.dto;

import com.kok.kokcore.station.domain.entity.Station;
import java.util.List;

public record StationRouteDtos(
    List<StationRouteDto> stationRouteDtos
) {

    public boolean isEmpty() {
        return stationRouteDtos().isEmpty();
    }

    public List<Station> toStations() {
        return stationRouteDtos.stream()
            .map(StationRouteDto::toStation)
            .toList();
    }
}
