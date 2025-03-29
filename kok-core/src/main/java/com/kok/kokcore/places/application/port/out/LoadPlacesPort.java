package com.kok.kokcore.places.application.port.out;


import com.kok.kokcore.places.application.port.in.PlaceInput;
import com.kok.kokcore.places.domain.model.PlacesResult;

public interface LoadPlacesPort {
    PlacesResult getPlaces(PlaceInput input);
}
