package com.kok.kokcore.places.domain.model;

import com.kok.kokcore.places.domain.model.vo.PlaceType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class Place {
    private final String name;
    private final String address;
    private final double latitude;
    private final double longitude;
    private final PlaceType placeType;
}