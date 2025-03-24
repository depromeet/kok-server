package com.kok.kokapi.vote.adapter.in.dto.response;

import com.kok.kokapi.public_transportation.adapter.in.dto.TmapPublicTransportationParsedResponse;
import com.kok.kokcore.station.domain.entity.Route;
import com.kok.kokcore.station.domain.entity.Station;
import java.util.List;

public record CandidateResponse(
    long stationId,
    String stationName,
    List<RouteResponse> routes,
    int totalTime,
    int transferCount,
    List<CommentResponse> comments
) {

    public static CandidateResponse of(
        Station station,
        List<Route> routes,
        TmapPublicTransportationParsedResponse transportationParsedResponse,
        List<CommentResponse> comments //comment 도메인 구현 시 List<Comment>로 교체
    ) {
        return new CandidateResponse(
            station.getId(),
            station.getName(),
            getRouteResponses(routes),
            transportationParsedResponse.totalTime(),
            transportationParsedResponse.transferCount(),
            comments
        );
    }

    private static List<RouteResponse> getRouteResponses(List<Route> routes) {
        return routes.stream().map(route -> new RouteResponse(route.getName())).toList();
    }
}
