package com.kok.kokapi.places.adapter.in.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kok.kokcore.places.domain.model.Place;
import com.kok.kokcore.places.domain.model.PlacesResult;
import java.util.stream.Collectors;

import java.util.List;
import lombok.Getter;
import lombok.ToString;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class PlacesResponse {
    private List<PlaceResponse> placeResponses;

    public PlacesResponse(PlacesResult placesResult) {
        this.placeResponses = placesResult.getPlaces()
            .stream()
            .map(PlaceResponse::new)
            .collect(Collectors.toList());
    }

    @Getter
    @ToString
    public static class PlaceResponse {
        private String displayName;
        private String formattedAddress;
        private Location location;

        public PlaceResponse(Place place) {
            this.displayName = place.getName();
            this.formattedAddress = place.getAddress();
            this.location = new Location(place.getLatitude(), place.getLongitude());
        }

        @Getter
        @ToString
        public static class Location {
            private double latitude;
            private double longitude;

            public Location(double latitude, double longitude) {
                this.latitude = latitude;
                this.longitude = longitude;
            }
        }
    }
}
