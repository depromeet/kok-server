package com.kok.kokapi.places.adapter.in.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlacesResponse {
    private List<PlaceResponse> placeResponses;

    @Data
    public static class PlaceResponse {
        private String displayName;
        private String formattedAddress;
        private Location location;

        @Data
        public static class Location {
            private double latitude;
            private double longitude;
        }
    }
}
