package com.kok.kokapi.place.adapter.out.external;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kok.kokcore.places.port.in.PlaceInput;
import com.kok.kokcore.places.port.out.LoadPlacesPort;
import com.kok.kokcore.places.domain.model.Place;
import com.kok.kokcore.places.domain.model.PlacesResult;
import com.kok.kokcore.places.domain.model.vo.PlaceType;
import java.util.stream.Collectors;
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
public class GooglePlaceApiAdapter implements LoadPlacesPort {

    @Value("${google.places.api.key}")
    private String apiKey;

    private static final String GOOGLE_PLACE_BASE_URL = "https://places.googleapis.com/v1/places:searchNearby";
    private static final double DEFAULT_RADIUS = 5000.0;

    private final ObjectMapper objectMapper;

    @Override
    public PlacesResult getPlaces(PlaceInput input) {
        String jsonBody = buildRequestBody(input);
        RestClient restClient = RestClient.create();

        try {
            String responseBody = restClient.method(HttpMethod.POST)
                .uri(GOOGLE_PLACE_BASE_URL)
                .header("Content-Type", "application/json")
                .header("X-Goog-Api-Key", apiKey)
                .header("X-Goog-FieldMask",
                    "places.displayName,places.formattedAddress,places.location").body(jsonBody)
                .retrieve()
                .body(String.class);

            return mapToPlacesResult(responseBody, input.placeType());
        } catch (IOException e) {
            log.error("Error while calling Google Places API", e);
            throw new RuntimeException("Call Google Places API Failed", e);
        }
    }

    private static String buildRequestBody(PlaceInput input) {
        String includedTypes = input.placeType().getPlaceCategories().stream()
            .map(category -> "\"" + category + "\"")
            .collect(Collectors.joining(", "));

        return String.format("""
                {
                    "includedTypes": [%s],
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
            includedTypes,
            input.maxCount(),
            input.latitude(),
            input.longitude(),
            DEFAULT_RADIUS);
    }

    private PlacesResult mapToPlacesResult(String responseBody, PlaceType placeType) throws JsonProcessingException {
        JsonNode root = objectMapper.readTree(responseBody);
        JsonNode placesNode = root.get("places");
        List<Place> places = new ArrayList<>();

        for (JsonNode node : placesNode) {
            String name = node.path("displayName").path("text").asText();
            String address = node.path("formattedAddress").asText();
            double latitude = node.path("location").path("latitude").asDouble();
            double longitude = node.path("location").path("longitude").asDouble();

            Place place = Place.builder()
                .name(name)
                .address(address)
                .latitude(latitude)
                .longitude(longitude)
                .placeType(placeType)
                .build();

            places.add(place);
        }
        return new PlacesResult(places);
    }
}