package com.kok.kokapi.public_transportation.adapter.in.dto;

public record RouteResponse(
        Integer totalTime,
        Integer transferCount
) {
    public static RouteResponse of(Integer totalTime, Integer transferCount) {
        return new RouteResponse(totalTime, transferCount);
    }
}
