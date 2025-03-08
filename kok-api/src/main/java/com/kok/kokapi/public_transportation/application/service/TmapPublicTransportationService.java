package com.kok.kokapi.public_transportation.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kok.kokapi.public_transportation.adapter.in.dto.TmapComplexPublicTransportationParsedResponse;
import com.kok.kokapi.public_transportation.adapter.in.dto.TmapPublicTransportationParsedResponse;
import com.kok.kokapi.public_transportation.adapter.out.external.dto.TmapComplexPublicTransportationResponse;
import com.kok.kokapi.public_transportation.adapter.out.external.PublicTransportationClient;
import com.kok.kokapi.public_transportation.adapter.out.external.PublicTransportationComplexClient;
import com.kok.kokapi.public_transportation.adapter.out.external.dto.TmapPublicTransportationResponse;
import com.kok.kokcore.public_transfortation.usecase.RetrievePublicTransportationUsecase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.kok.kokapi.public_transportation.adapter.in.dto.TmapComplexPublicTransportationParsedResponse.*;
import static com.kok.kokapi.public_transportation.adapter.out.external.dto.TmapComplexPublicTransportationResponse.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class TmapPublicTransportationService implements RetrievePublicTransportationUsecase {

    private final PublicTransportationClient publicTransportationClient;
    private final PublicTransportationComplexClient publicTransportationComplexClient;
    private final ObjectMapper objectMapper;


    @Cacheable(value = "sub", cacheManager = "contentCacheManager", key = "'PTSubCache:' + #stationId + '-' + #UUID + '-' + #memberId")
    @Override
    public String retrievePublicTransportation(Long stationId, String UUID, Integer memberId) {
        TmapPublicTransportationResponse rawRoute = publicTransportationClient.callPublicTransportRoute(stationId, UUID, memberId);
        try {
            return objectMapper.writeValueAsString(parseTmapResponse(rawRoute));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("파싱 실패..");
        }
    }

    @Cacheable(value = "complex", cacheManager = "contentCacheManager", key = "'PTComplexCache:' + #stationId + '-' + #UUID + '-' + #memberId")
    @Override
    public String retrieveComplexPublicTransportation(Long stationId, String UUID, Integer memberId) {
        TmapComplexPublicTransportationResponse rawRoute = publicTransportationComplexClient.callComplexPublicTransportRoute(stationId, UUID, memberId);
        try {
            return objectMapper.writeValueAsString(parseComplexTmapResponse(rawRoute));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("파싱 실패..");
        }
    }

    public TmapComplexPublicTransportationParsedResponse parseComplexTmapResponse(TmapComplexPublicTransportationResponse response) {
        if (response == null || response.getMetaData() == null || response.getMetaData().getPlan() == null
                || response.getMetaData().getPlan().getItineraries() == null || response.getMetaData().getPlan().getItineraries().isEmpty()) {
            return null;
        }

        Itinerary itinerary = response.getMetaData().getPlan().getItineraries().getFirst();
        ParsedItinerary parsedItinerary = new ParsedItinerary();
        parsedItinerary.setTotalDistance(itinerary.getTotalDistance());
        parsedItinerary.setTotalTime(itinerary.getTotalTime());

        List<ParsedLeg> parsedLegs = itinerary.getLegs().stream().map(leg -> {
            ParsedLeg parsedLeg = new ParsedLeg();
            parsedLeg.setMode(leg.getMode());
            parsedLeg.setDistance(leg.getDistance());
            parsedLeg.setSectionTime(leg.getSectionTime());
            parsedLeg.setRoute(leg.getRoute());
            parsedLeg.setRouteColor(leg.getRouteColor());
            return parsedLeg;
        }).collect(Collectors.toList());

        parsedItinerary.setLegs(parsedLegs);

        TmapComplexPublicTransportationParsedResponse parsedRequest = new TmapComplexPublicTransportationParsedResponse();
        parsedRequest.setParsedItinerary(parsedItinerary);
        return parsedRequest;
    }

    public TmapPublicTransportationParsedResponse parseTmapResponse(TmapPublicTransportationResponse response) {
        if (response == null || response.getMetaData() == null || response.getMetaData().getPlan() == null || response.getMetaData().getPlan().getItineraries() == null ) {
            return null;
        }
        return TmapPublicTransportationParsedResponse.of(
                response.getMetaData().getPlan().getItineraries().getFirst().getTotalTime(),
                response.getMetaData().getPlan().getItineraries().getFirst().getTransferCount());
    }

}
