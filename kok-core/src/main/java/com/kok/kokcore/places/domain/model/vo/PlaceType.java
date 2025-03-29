package com.kok.kokcore.places.domain.model.vo;

import java.util.List;
import lombok.Getter;

/**
 * Place 유형 Enum
 */
@Getter
public enum PlaceType {
    ACTIVITY(List.of(
        "amusement_park", "aquarium", "art_gallery", "bowling_alley", "gym", "movie_theater",
        "museum", "night_club", "spa", "stadium", "tourist_attraction", "zoo"
    )),
    RESTAURANT(List.of(
        "restaurant", "meal_delivery", "meal_takeaway"
    )),
    CAFE(List.of(
        "cafe", "bakery"
    )),
    BAR(List.of("bar")),
    PARK(List.of(
        "park", "campground", "rv_park"
    )),
    CULTURE(List.of(
        "library", "church", "hindu_temple", "mosque", "synagogue"
    )),
    EVENT(List.of(
        "casino", "movie_theater", "stadium"
    )),
    SHOPPING(List.of(
        "shopping_mall", "clothing_store", "convenience_store", "department_store",
        "electronics_store", "furniture_store", "hardware_store", "home_goods_store",
        "jewelry_store", "liquor_store", "shoe_store", "store", "supermarket"
    ));

    private final List<String> placeCategories;

    PlaceType(List<String> placeCategories) {
        this.placeCategories = placeCategories;
    }
}