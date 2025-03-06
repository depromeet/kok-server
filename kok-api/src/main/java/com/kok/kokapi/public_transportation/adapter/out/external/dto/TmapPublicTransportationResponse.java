package com.kok.kokapi.public_transportation.adapter.out.external.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

// Tmap 처리를 위한 POJO
@Getter
@Setter
public class TmapPublicTransportationResponse {

    // MetaData -> RequestParameters, Plan
    // Plan -> Itinerary
    // Itinerary -> Fare, totalTime, totalWalkTime, pathType, transferCount, totalDistance, totalWalkDistance
    // 형식과 다른 Json은 무시.

    private MetaData metaData;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @Setter
    public static class MetaData {
        private RequestParameters requestParameters;
        private Plan plan;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @Setter
    public static class RequestParameters {
        private String endY;
        private String endX;
        private String startY;
        private String startX;
        private String reqDttm;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @Setter
    public static class Plan {
        private List<Itinerary> itineraries;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @Setter
    public static class Itinerary {
        private Fare fare;
        private int totalTime;
        private int totalWalkTime;
        private int pathType;
        private int transferCount;
        private int totalDistance;
        private int totalWalkDistance;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @Setter
    public static class Fare {
        private RegularFare regular;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @Setter
    public static class RegularFare {
        private int totalFare;
        private Currency currency;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @Setter
    public static class Currency {
        private String symbol;
        private String currency;
        private String currencyCode;

    }
}
