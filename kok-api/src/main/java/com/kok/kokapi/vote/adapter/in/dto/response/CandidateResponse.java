package com.kok.kokapi.vote.adapter.in.dto.response;

import com.kok.kokapi.public_transportation.adapter.in.dto.response.TmapPublicTransportationParsedResponse;
import com.kok.kokcore.station.domain.entity.Route;
import com.kok.kokcore.station.domain.entity.Station;
import java.util.List;

public record CandidateResponse(
    long stationId,
    String stationName,
    List<String> routes,
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
            getRoutes(routes),
            transportationParsedResponse.totalTime(),
            transportationParsedResponse.transferCount(),
            comments
        );
    }

    private static List<String> getRoutes(List<Route> routes) {
        return routes.stream().map(Route::getName).toList();
    }
}
