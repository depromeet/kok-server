package com.kok.kokcore.places.domain.model;

import lombok.Data;

import java.util.List;

@Data
public class PlacesResult {
    private List<Place> places;
}
