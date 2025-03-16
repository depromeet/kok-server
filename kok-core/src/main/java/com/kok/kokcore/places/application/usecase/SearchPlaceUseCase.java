package com.kok.kokcore.places.application.usecase;

import com.kok.kokcore.places.application.port.in.PlaceInput;
import com.kok.kokcore.places.domain.model.PlacesResult;


public interface SearchPlaceUseCase {
    PlacesResult getPlaces(PlaceInput input) throws Exception;
}
