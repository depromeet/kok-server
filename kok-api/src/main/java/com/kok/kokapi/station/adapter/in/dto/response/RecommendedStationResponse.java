package com.kok.kokapi.station.adapter.in.dto.response;

import com.kok.kokcore.station.domain.entity.Route;
import com.kok.kokcore.station.domain.entity.Station;
import java.util.List;

public record RecommendedStationResponse(

    List<String> routes,
    Station station
) {

    public static RecommendedStationResponse of(Station station, List<Route> routes) {
        return new RecommendedStationResponse(
            routes.stream().map(Route::getName).toList(),
            station
        );
    }
}
