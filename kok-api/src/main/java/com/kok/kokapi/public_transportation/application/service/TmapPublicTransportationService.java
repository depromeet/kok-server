package com.kok.kokapi.public_transportation.application.service;

import com.kok.kokapi.centroid.adapter.out.persistence.LocationPersistenceAdapter;
import com.kok.kokapi.config.geometry.PointConverter;
import com.kok.kokapi.public_transportation.adapter.out.external.PublicTransportationClient;
import com.kok.kokapi.public_transportation.adapter.out.external.dto.TmapPublicTransportationResponse;
import com.kok.kokapi.station.adapter.out.persistence.StationPersistenceAdapter;
import com.kok.kokcore.location.domain.Location;
import com.kok.kokcore.public_transfortation.usecase.RetrievePublicTransportationUsecase;
import com.kok.kokcore.station.domain.entity.Station;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class TmapPublicTransportationService implements RetrievePublicTransportationUsecase {

    private final PublicTransportationClient publicTransportationClient;
    private final StationPersistenceAdapter stationPersistenceAdapter;
    private final LocationPersistenceAdapter locationPersistenceAdapter;
    private final PointConverter pointConverter;


    @Cacheable(value = "publicTransportation", cacheManager = "contentCacheManager", key = "'PTCache:' + #stationId + '-' + #UUID + '-' + #memberId")
    @Override
    public List<Integer> retrievePublicTransportation(Long stationId, String UUID, Integer memberId) {
        TmapPublicTransportationResponse rawRoute = callPublicTransportRoute(stationId, UUID, memberId);
        return List.of(
                rawRoute.getMetaData().getPlan().getItineraries().getFirst().getTotalTime(), // 총 소요 시간
                rawRoute.getMetaData().getPlan().getItineraries().getFirst().getTransferCount() // 환승 횟수
        );
    }

    public TmapPublicTransportationResponse callPublicTransportRoute(Long stationId, String UUID, Integer memberId){
        log.info("Tmap api call : {}-{}-{}", stationId, UUID, memberId);
        return publicTransportationClient.getClient().post()
                .body(buildRequestBody(
                        getUserLocation(UUID, memberId),
                        getStation(stationId)))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (status, response) -> {
                    throw new RuntimeException("Tmap api 호출에 실패했습니다." + status);
                })
                .body(TmapPublicTransportationResponse.class);
    }

    private Pair<BigDecimal, BigDecimal> getUserLocation(String UUID, Integer memberId) {
        Location userPoint = locationPersistenceAdapter.findLocationByUuidAndMemberId(UUID, memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 UUID의 사용자 위치가 존재하지 않습니다."));
        return pointConverter.toCoordinates(userPoint.getPoint());
    }

    private Station getStation(Long stationId) {
        return stationPersistenceAdapter.retrieveStation(stationId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 역이 존재하지 않습니다."));
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

}
