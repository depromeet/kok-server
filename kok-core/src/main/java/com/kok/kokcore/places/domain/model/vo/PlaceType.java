package com.kok.kokcore.places.domain.model.vo;

import lombok.Getter;

/**
 * Place 유형 Enum
 */
@Getter
public enum PlaceType {
    RESTAURANT("restaurant"),
    CAFE("cafe"),
    DEPARTMENT_STORE("department_store"),
    BAR("bar"),
    TOURIST_ATTRACTION("tourist_attraction"),
    PARK("park"),
    CONCERT_HALL("concert_hall"),
    SHOPPING_MALL("shopping_mall"),
    CLUB("night_club"),
    MUSEUM("museum"),
    ART_GALLERY("art_gallery")
    ;

    private final String type;

    PlaceType(String type) {
        this.type = type;
    }
}
