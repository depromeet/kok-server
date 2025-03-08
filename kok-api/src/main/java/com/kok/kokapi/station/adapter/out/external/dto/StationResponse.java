package com.kok.kokapi.station.adapter.out.external.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kok.kokcore.station.application.port.out.dto.StationRouteDto;

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

    public StationRouteDto toStationRouteDto() {
        return new StationRouteDto(name, latitude, longitude, id, route);
    }
}
