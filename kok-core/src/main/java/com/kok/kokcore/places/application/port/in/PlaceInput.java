package com.kok.kokcore.places.application.port.in;

import com.kok.kokcore.places.domain.model.vo.PlaceType;

public record PlaceInput(
        PlaceType placeType,
        double latitude,
        double longitude,
        Integer maxCount
) {}
