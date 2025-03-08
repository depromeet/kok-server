package com.kok.kokapi.centroid.adapter.in.dto.response;

import java.util.List;

public record ConvexHullLocationResponse(
        List<LocationResponse> convexHull,
        List<LocationResponse> inside
) {
    public static ConvexHullLocationResponse of(List<LocationResponse> convexHull, List<LocationResponse> inside) {
        return new ConvexHullLocationResponse(convexHull, inside);
    }
}
