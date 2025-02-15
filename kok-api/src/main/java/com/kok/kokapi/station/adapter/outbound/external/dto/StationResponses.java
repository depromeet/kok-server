package com.kok.kokapi.station.adapter.outbound.external.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kok.kokcore.station.domain.entity.Station;
import java.util.List;

public record StationResponses(
    @JsonProperty("subwayStationMaster")
    SubwayStationMaster subwayStationMaster
) {

    public List<Station> toStations() {
        return subwayStationMaster.row().stream()
            .map(StationResponse::toStation)
            .toList();
    }
}
