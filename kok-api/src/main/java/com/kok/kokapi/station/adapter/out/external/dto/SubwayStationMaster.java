package com.kok.kokapi.station.adapter.out.external.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record SubwayStationMaster(
    @JsonProperty("list_total_count")
    long listTotalCount,
    @JsonProperty("RESULT")
    Result result,
    @JsonProperty("row")
    List<StationResponse> row
) {

}
