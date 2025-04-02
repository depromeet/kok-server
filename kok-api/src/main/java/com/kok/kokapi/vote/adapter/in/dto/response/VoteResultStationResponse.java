package com.kok.kokapi.vote.adapter.in.dto.response;

import com.kok.kokcore.station.domain.entity.Route;
import com.kok.kokcore.station.domain.entity.Station;
import java.math.BigDecimal;
import java.util.List;

public record VoteResultStationResponse(
    long id,
    String name,
    BigDecimal latitude,
    BigDecimal longitude,
    long priority,
    List<String> routes
) {

    public static VoteResultStationResponse of(Station station, List<Route> routes) {
        return new VoteResultStationResponse(
            station.getId(),
            station.getName(),
            station.getLatitude(),
            station.getLongitude(),
            station.getPriority(),
            routes.stream().map(Route::getName).toList()
        );
    }
}
