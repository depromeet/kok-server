package com.kok.kokapi.place.adapter.in.dto.response;

import com.kok.kokcore.places.domain.model.Place;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PlaceResponse {
    private final String displayName;
    private final String formattedAddress;
    private final Location location;

    public PlaceResponse(Place place) {
        this.displayName = place.getName();
        this.formattedAddress = place.getAddress();
        this.location = new Location(place.getLatitude(), place.getLongitude());
    }

    @Getter
    @ToString
    public static class Location {
        private final double latitude;
        private final double longitude;

        public Location(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }
}