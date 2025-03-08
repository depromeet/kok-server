package com.kok.kokcore.station.application.port.out.dto;

import com.kok.kokcore.station.domain.entity.Route;
import com.kok.kokcore.station.domain.entity.Station;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public record StationRouteDtos(
    List<StationRouteDto> stationRouteDtos
) {

    public boolean isEmpty() {
        return stationRouteDtos().isEmpty();
    }

    public List<Station> toStations() {
        return distinctByName().stream()
            .map(StationRouteDto::toStation)
            .toList();
    }

    private List<StationRouteDto> distinctByName() {
        return new ArrayList<>(stationRouteDtos.stream()
            .collect(Collectors.toMap(
                StationRouteDto::name,
                dto -> dto,
                (existing, replacement) -> existing
            ))
            .values());
    }

    public List<Route> toRoutesByStations(List<Station> stations){
        List<Route> routes = new ArrayList<>();
        for (Station station : stations) {
            List<Route> routesOfStation = stationRouteDtos.stream()
                .filter(stationRouteDto -> stationRouteDto.hasName(station))
                .map(stationRouteDto -> stationRouteDto.toRouteByStation(station))
                .toList();
            routes.addAll(routesOfStation);
        }
        return routes;
    }
}
