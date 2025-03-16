package com.kok.kokapi.places.adapter.out.external;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kok.kokcore.places.application.port.in.PlaceInput;
import com.kok.kokcore.places.application.port.out.LoadPlacesPort;
import com.kok.kokcore.places.domain.model.Place;
import com.kok.kokcore.places.domain.model.PlacesResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class GooglePlaceAdapter implements LoadPlacesPort {


    @Value("${google.places.api.key}")
    private String apiKey;

    private static final String GOOGLE_PLACE_BASE_URL = "https://places.googleapis.com/v1/places:searchNearby";
    public static final double DEFAULT_RADIUS = 5000.0;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public PlacesResult getPlaces(PlaceInput input) {
        int maxResultCount = input.maxCount() != null ? input.maxCount() : 20;

        String jsonBody = String.format("""
                        {
                            "includedTypes": ["%s"],
                            "maxResultCount": %d,
                            "locationRestriction": {
                                "circle": {
                                    "center": {
                                        "latitude": %.6f,
                                        "longitude": %.6f
                                    },
                                    "radius": %.1f
                                }
                            },
                            "languageCode": "ko"
                        }
                        """,
                input.placeType().getType(),
                maxResultCount,
                input.latitude(),
                input.longitude(),
                DEFAULT_RADIUS
        );

        RestClient restClient = RestClient.create();

        try {
            String responseBody = restClient.method(HttpMethod.POST)
                    .uri(GOOGLE_PLACE_BASE_URL)
                    .header("Content-Type", "application/json")
                    .header("X-Goog-Api-Key", apiKey)
                    .header("X-Goog-FieldMask", "places.displayName,places.formattedAddress,places.location")
                    .body(jsonBody)
                    .retrieve()
                    .body(String.class);

            log.debug("Google Places API Response: {}", responseBody);

            return mapToPlacesResult(responseBody);
        } catch (IOException e) {
            log.error("Error while calling Google Places API", e);
            throw new RuntimeException("Google Places API 호출 실패", e);
        }
    }

    private PlacesResult mapToPlacesResult(String responseBody) throws JsonProcessingException {
        JsonNode root = objectMapper.readTree(responseBody);
        JsonNode placesNode = root.get("places");
        List<Place> places = new ArrayList<>();

        for (JsonNode node : placesNode) {
            String name = node.path("displayName").path("text").asText();
            String address = node.path("formattedAddress").asText();
            double latitude = node.path("location").path("latitude").asDouble();
            double longitude = node.path("location").path("longitude").asDouble();

            Place place = new Place();
            place.setName(name);
            place.setAddress(address);
            place.setLatitude(latitude);
            place.setLongitude(longitude);

            places.add(place);
        }

        PlacesResult result = new PlacesResult();
        result.setPlaces(places);
        return result;
    }
}
