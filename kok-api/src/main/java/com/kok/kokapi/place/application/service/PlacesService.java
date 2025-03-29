package com.kok.kokapi.place.application.service;

import com.kok.kokcore.places.port.in.PlaceInput;
import com.kok.kokcore.places.port.out.LoadPlacesPort;
import com.kok.kokcore.places.usecase.SearchPlaceUseCase;
import com.kok.kokcore.places.domain.model.PlacesResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlacesService implements SearchPlaceUseCase {

    private final LoadPlacesPort loadPlacesPort;

    @Override
    public PlacesResult getPlaces(PlaceInput input) {
        return loadPlacesPort.getPlaces(input);
    }
}