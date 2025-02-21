package com.kok.kokapi.station.adapter.out.external.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Result(
    @JsonProperty("CODE")
    String code,
    @JsonProperty("MESSAGE")
    String message
) {

}
