package com.kok.kokapi.places.adapter.in.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kok.kokcore.places.domain.model.PlacesResult;
import java.util.stream.Collectors;

import java.util.List;
import lombok.Getter;
import lombok.ToString;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class PlacesResponse {
    private final List<PlaceResponse> placeResponses;

    public PlacesResponse(PlacesResult placesResult) {
        this.placeResponses = placesResult.places()
            .stream()
            .map(PlaceResponse::new)
            .collect(Collectors.toList());
    }
}
