package com.kok.kokapi.public_transportation.adapter.out.external;

import com.kok.kokapi.config.geometry.PointConverter;
import com.kok.kokapi.public_transportation.adapter.out.external.dto.TmapPublicTransportationResponse;
import com.kok.kokcore.location.application.port.out.ReadLocationPort;
import com.kok.kokcore.location.domain.Location;
import com.kok.kokcore.station.application.port.out.RetrieveStationsPort;
import com.kok.kokcore.station.domain.entity.Station;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.ClientHttpRequestFactorySettings;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;


@Component
@EnableConfigurationProperties(TmapClientProperties.class)
@Slf4j
public class PublicTransportationClient {

    private final RestClient restClient;
    private final TmapClientProperties properties;

    private final RetrieveStationsPort retrieveStationsPort;
    private final ReadLocationPort readLocationPort;
    private final PointConverter pointConverter;

    public PublicTransportationClient(TmapClientProperties properties, RetrieveStationsPort retrieveStationsPort, ReadLocationPort readLocationPort, PointConverter pointConverter) {
        this.properties = properties;
        this.retrieveStationsPort = retrieveStationsPort;
        this.readLocationPort = readLocationPort;
        this.pointConverter = pointConverter;
        this.restClient = getRestClient();
    }

    public RestClient getRestClient() {
        return RestClient.builder()
                .requestFactory(getRequestFactory())
                .defaultHeader(properties.keyname(), properties.key())  // API Key 추가
                .baseUrl(properties.url()) // Base URL 설정
                .build();
    }

    public RestClient getClient() {
        return this.restClient;
    }

    private ClientHttpRequestFactory getRequestFactory() {
        return ClientHttpRequestFactoryBuilder.detect()
                .build(ClientHttpRequestFactorySettings.defaults());
    }

    public TmapPublicTransportationResponse callPublicTransportRoute(Long stationId, String UUID, Integer memberId) {
        log.info("Tmap api call : {}-{}-{}", stationId, UUID, memberId);
        return getClient().post()
                .body(buildRequestBody(
                        getUserLocation(UUID, memberId),
                        getStation(stationId)))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (status, response) -> {
                    throw new RuntimeException("Tmap api 호출에 실패했습니다. by 4xx" + status);
                })
                .onStatus(HttpStatusCode::is5xxServerError, (status, response) -> {
                    throw new RuntimeException("Tmap api 호출에 실패했습니다. by 5xx" + status);
                })
                .body(TmapPublicTransportationResponse.class);
    }

    private Map<String, Object> buildRequestBody(Pair<BigDecimal, BigDecimal> userLocation, Station station) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("startX", userLocation.getSecond()); // 경도
        requestBody.put("startY", userLocation.getFirst()); // 위도
        requestBody.put("endX", station.getLongitude()); // 경도
        requestBody.put("endY", station.getLatitude()); // 위도
        requestBody.put("count", 1); // 경로 탐색 결과 중 1개만 반환
        requestBody.put("format", "json");
        return requestBody;
    }

    private Pair<BigDecimal, BigDecimal> getUserLocation(String UUID, Integer memberId) {
        Location userPoint = readLocationPort.findLocationByUuidAndMemberId(UUID, memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 UUID의 사용자 위치가 존재하지 않습니다."));
        return pointConverter.toCoordinates(userPoint.getLocation_point());
    }

    private Station getStation(Long stationId) {
        return retrieveStationsPort.retrieveStation(stationId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 역이 존재하지 않습니다."));
    }
}
