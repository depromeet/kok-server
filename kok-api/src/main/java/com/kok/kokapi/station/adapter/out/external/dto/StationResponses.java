package com.kok.kokapi.station.adapter.out.external.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kok.kokcore.station.application.port.out.dto.StationRouteDtos;

public record StationResponses(
    @JsonProperty("subwayStationMaster")
    SubwayStationMaster subwayStationMaster
) {

    public StationRouteDtos toStationRouteDtos() {
        return new StationRouteDtos(subwayStationMaster.row().stream()
            .map(StationResponse::toStationRouteDto)
            .toList());
    }
}
