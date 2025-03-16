package com.kok.kokcore.places.domain.model.vo;

import lombok.Getter;

/**
 * Place 유형 Enum
 */
@Getter
public enum PlaceType {
    RESTAURANT("restaurant"),
    CAFE("cafe"),
    DEPARTMENT_STORE("department_store");

    private final String type;

    PlaceType(String type) {
        this.type = type;
    }
}
