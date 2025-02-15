package com.kok.kokapi.station.adapter.outbound.external.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kok.kokcore.station.domain.entity.Station;

public record StationResponse(
    @JsonProperty("BLDN_ID")
    long id,
    @JsonProperty("BLDN_NM")
    String name,
    @JsonProperty("ROUTE")
    String route,
    @JsonProperty("LAT")
    String latitude,
    @JsonProperty("LOT")
    String longitude
) {

    public Station toStation() {
        return new Station(id, name, route, latitude, longitude);
    }
}
