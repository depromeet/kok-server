package com.kok.kokapi.station.application.service;

import com.kok.kokapi.config.geometry.PointConverter;
import com.kok.kokcore.station.application.port.out.*;
import com.kok.kokcore.station.application.port.out.dto.StationRouteDtos;
import com.kok.kokcore.station.application.usecase.RecommendStationUseCase;
import com.kok.kokcore.station.application.usecase.SaveStationUseCase;
import com.kok.kokcore.station.domain.entity.Station;

import java.math.BigDecimal;
import java.util.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class StationService implements SaveStationUseCase, RecommendStationUseCase {

    private final LoadStationsPort loadStationsPort;
    private final SaveStationsPort saveStationsPort;
    private final ReadStationsPort readStationsPort;
    private final SaveRoutePort saveRoutePort;
    private final RetrieveStationsPort retrieveStationsPort;
    private final PointConverter pointConverter;

    @Override
    @Transactional
    public void saveStations() {
        if(readStationsPort.hasNoStations()) {
            StationRouteDtos stationRouteDtos = loadStationsPort.loadAllStations();
            List<Station> stations = saveStationsPort.saveStations(stationRouteDtos.toStations());
            saveRoutePort.saveRoutes(stationRouteDtos.toRoutesByStations(stations));
        }
    }

    @Override
    public List<Station> recommendStations(Point centroid) {
        double dist = 100;
        List<Station> stations = List.of();

        // 최대 탐색 거리 제한 10km
        int VOTE_NUM = 2;
        while (dist < 10000) {
            stations = retrieveStationsPort.retrieveInRangeStations(centroid, dist);
            log.info("Counter : " + stations.size() + " Dist : " + dist);
            if (stations.size() >= VOTE_NUM) {
                break;
            }
            dist *= 1.5;
        }

        // 역을 찾지 못한 경우 예외 처리
        if (stations.isEmpty()) {
            throw new RuntimeException("범위 내에 지하철역이 없습니다.");
        }

        // 거리 및 가중치 기반 확률 계산
        Map<Station, Double> probabilityMap = calculateProbabilities(stations, centroid);

        // 확률에 따라 랜덤으로 상위 2개 지하철 선택
        return selectTopStations(probabilityMap, VOTE_NUM);
    }

    private Map<Station, Double> calculateProbabilities(List<Station> stations, Point centroid) {
        Map<Station, Double> weightedDistances = new HashMap<>();
        double distanceSum = 0;
        double totalPriority = 0;

        for (Station station : stations) {
            double distance = calculateDistance(centroid, station);
            if (distance == 0) distance = Double.MIN_VALUE; // 0 거리 방지

            double weight = (1 / distance) * station.getPriority();
            weightedDistances.put(station, weight);
            distanceSum += (1 / distance);
            totalPriority += station.getPriority();
        }

        // 확률 계산
        Map<Station, Double> probabilityMap = new HashMap<>();
        for (Map.Entry<Station, Double> entry : weightedDistances.entrySet()) {
            probabilityMap.put(entry.getKey(), entry.getValue() / (distanceSum * totalPriority));
        }

        return probabilityMap;
    }

    private List<Station> selectTopStations(Map<Station, Double> probabilityMap, int count) {
        List<Station> selectedStations = new ArrayList<>();
        Random random = new Random();

        while (selectedStations.size() < count && !probabilityMap.isEmpty()) {
            double rand = random.nextDouble();
            double cumulativeProbability = 0;
            Station selectedStation = null;

            for (Map.Entry<Station, Double> entry : probabilityMap.entrySet()) {
                cumulativeProbability += entry.getValue();
                if (rand <= cumulativeProbability) {
                    selectedStation = entry.getKey();
                    break;
                }
            }

            if (selectedStation != null) {
                selectedStations.add(selectedStation);
                probabilityMap.remove(selectedStation);
            }
        }

        return selectedStations;
    }

//    //정교한 미터 거리 (Haversine) 계산
//    private double calculateDistance(Point p1, Station station) {
//        Pair<BigDecimal, BigDecimal> coordinates = pointConverter.toCoordinates(p1);
//        double lat1 = coordinates.getFirst().doubleValue();
//        double lon1 = coordinates.getSecond().doubleValue();
//        double lat2 = station.getLatitude().doubleValue();
//        double lon2 = station.getLongitude().doubleValue();
//
//        // Haversine 공식 사용 (단위: 미터)
//        double R = 6371000;
//        double dLat = Math.toRadians(lat2 - lat1);
//        double dLon = Math.toRadians(lon2 - lon1);
//        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
//                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
//                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
//        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
//
//        return R * c; // 결과: 미터 단위 거리
//    }

    // 유클리드 거리 기반.
    private double calculateDistance(Point p1, Station station) {
        Pair<BigDecimal, BigDecimal> coordinates = pointConverter.toCoordinates(p1);
        double lat1 = coordinates.getFirst().doubleValue();
        double lon1 = coordinates.getSecond().doubleValue();
        double lat2 = station.getLatitude().doubleValue();
        double lon2 = station.getLongitude().doubleValue();

        // 유클리드 거리 공식 적용
        return Math.sqrt(Math.pow(lat2 - lat1, 2) + Math.pow(lon2 - lon1, 2));
    }
}
