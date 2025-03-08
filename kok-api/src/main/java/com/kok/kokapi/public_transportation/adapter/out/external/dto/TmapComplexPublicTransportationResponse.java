package com.kok.kokapi.public_transportation.adapter.out.external.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TmapComplexPublicTransportationResponse {
    @JsonProperty("metaData")
    private MetaData metaData;

    @Getter
    @Setter
    public static class MetaData {
        @JsonProperty("requestParameters")
        private RequestParameters requestParameters;

        @JsonProperty("plan")
        private Plan plan;
    }

    @Getter
    @Setter
    public static class RequestParameters {
        private int busCount;
        private int expressbusCount;
        private int subwayCount;
        private int airplaneCount;
        private String locale;
        private double endY;
        private double endX;
        private int wideareaRouteCount;
        private int subwayBusCount;
        private double startY;
        private double startX;
        private int ferryCount;
        private int trainCount;
        private String reqDttm;
    }

    @Getter
    @Setter
    public static class Plan {
        @JsonProperty("itineraries")
        private List<Itinerary> itineraries;
    }

    @Getter
    @Setter
    public static class Itinerary {
        @JsonProperty("fare")
        private Fare fare;

        private int totalTime;
        private List<Leg> legs;
        private int totalWalkTime;
        private int transferCount;
        private int totalDistance;
        private int pathType;
        private int totalWalkDistance;
    }

    @Getter
    @Setter
    public static class Fare {
        @JsonProperty("regular")
        private RegularFare regular;
    }

    @Getter
    @Setter
    public static class RegularFare {
        private int totalFare;
        private Currency currency;
    }

    @Getter
    @Setter
    public static class Currency {
        private String symbol;
        private String currency;
        private String currencyCode;
    }

    @Getter
    @Setter
    public static class Leg {
        private String mode;
        private int sectionTime;
        private int distance;
        private Location start;
        private Location end;
        private List<Step> steps;
        private String routeColor;
        private String route;
        private String routeId;
        private int service;
        private PassStopList passStopList;
        private int type;
        private PassShape passShape;
    }

    @Getter
    @Setter
    public static class Location {
        private String name;
        private double lon;
        private double lat;
    }

    @Getter
    @Setter
    public static class Step {
        private String streetName;
        private int distance;
        private String description;
        private String linestring;
    }

    @Getter
    @Setter
    public static class PassStopList {
        private List<Station> stationList;
    }

    @Getter
    @Setter
    public static class Station {
        private int index;
        private String stationName;
        private String lon;
        private String lat;
        private String stationID;
    }

    @Getter
    @Setter
    public static class PassShape {
        private String linestring;
    }

}
