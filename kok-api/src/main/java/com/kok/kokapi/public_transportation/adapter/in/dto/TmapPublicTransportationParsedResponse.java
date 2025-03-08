package com.kok.kokapi.public_transportation.adapter.in.dto;

public record TmapPublicTransportationParsedResponse(
        Integer totalTime,
        Integer transferCount
) {
    public static TmapPublicTransportationParsedResponse of(Integer totalTime, Integer transferCount) {
        return new TmapPublicTransportationParsedResponse(totalTime, transferCount);
    }
}
