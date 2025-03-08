package com.kok.kokapi.public_transportation.adapter.in.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TmapComplexPublicTransportationParsedResponse implements Serializable {

    private ParsedItinerary parsedItinerary;
    // DTO 클래스 정의
    @Getter
    @Setter
    public static class ParsedItinerary {
        private int totalDistance;
        private int totalTime;
        private List<ParsedLeg> legs;
    }

    @Getter
    @Setter
    public static class ParsedLeg {
        private String mode;
        private int distance;
        private int sectionTime;
        private String route; // 지하철이면 호선 정보, 버스면 노선 정보
        private String routeColor; // 노선 색

    }
}
