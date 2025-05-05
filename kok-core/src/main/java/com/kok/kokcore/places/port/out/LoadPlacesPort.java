package com.kok.kokcore.places.port.out;


import com.kok.kokcore.places.port.in.PlaceInput;
import com.kok.kokcore.places.domain.model.PlacesResult;

public interface LoadPlacesPort {
    PlacesResult getPlaces(PlaceInput input);
}