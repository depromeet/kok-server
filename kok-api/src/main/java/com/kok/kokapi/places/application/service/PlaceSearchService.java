package com.kok.kokapi.places.application.service;

import com.kok.kokcore.places.application.port.in.PlaceInput;
import com.kok.kokcore.places.application.port.out.LoadPlacesPort;
import com.kok.kokcore.places.application.usecase.SearchPlaceUseCase;
import com.kok.kokcore.places.domain.model.PlacesResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlaceSearchService implements SearchPlaceUseCase {

    private final LoadPlacesPort loadPlacesPort;

    @Override
    public PlacesResult getPlaces(PlaceInput input) {
        return loadPlacesPort.getPlaces(input);
    }
}
