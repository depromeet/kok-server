//package com.kok.kokapi.centroid.application.service;
//
//import com.kok.kokapi.centroid.adapter.out.persistence.LocationPersistenceAdapter;
//import com.kok.kokapi.config.geometry.PointConverter;
//import com.kok.kokcore.application.domain.entity.Location;
//import com.kok.kokcore.application.usecase.CreateLocationUsecase;
//import com.kok.kokcore.application.usecase.ReadCentroidUsecase;
//import com.kok.kokcore.application.usecase.ReadLocationUsecase;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import org.locationtech.jts.geom.Point;
//import org.springframework.data.util.Pair;
//import org.springframework.stereotype.Service;
//
//import java.math.BigDecimal;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class LocationService implements CreateLocationUsecase, ReadCentroidUsecase, ReadLocationUsecase {
//
//    private final LocationPersistenceAdapter locationRepository;
//    private final PointConverter pointConverter;
//
//    @Override
//    public Location createLocation(String uuid, Integer memberId, BigDecimal latitude, BigDecimal longitude) {
//        Point point = pointConverter.fromCoordinates(latitude, longitude);
//        return locationRepository.saveLocation(uuid, memberId, point);
//    }
//
//    @Override
//    @Transactional
//    public Location updateLocation(String uuid, Integer memberId, BigDecimal latitude, BigDecimal longitude) {
//        Location location = locationRepository.findLocationByUuidAndMemberId(uuid, memberId)
//                .orElseThrow(() -> new RuntimeException("해당 ID의 정보가 없습니다."));
//        Point newPoint = pointConverter.fromCoordinates(latitude, longitude);
//        location.changePoint(newPoint);
//        return location;
//    }
//
//    @Override
//    public Point readCentroid(String uuid) {
//        return locationRepository.findCentroidByUuid(uuid);
//    }
//
//    @Override
//    public Pair<BigDecimal, BigDecimal> readCentroidCoordinates(String uuid) {
//        Point centroidPoint = locationRepository.findCentroidByUuid(uuid);
//        if (centroidPoint == null) {
//            throw new IllegalArgumentException("해당 UUID에 대한 중심점을 찾을 수 없습니다.");
//        }
//        return pointConverter.toCoordinates(centroidPoint);
//    }
//
//    @Override
//    public Location readLocation(String uuid, Integer memberId) {
//        return locationRepository.findLocationByUuidAndMemberId(uuid, memberId)
//                .orElseThrow(() -> new IllegalArgumentException("해당 멤버의 위치를 찾을 수 없습니다."));
//    }
//
//    @Override
//    public List<Location> readLocations(String uuid) {
//        List<Location> locations = locationRepository.findLocationsByUuid(uuid);
//        if (locations.isEmpty()) {
//            throw new IllegalArgumentException("해당 UUID에 대한 위치들을 찾을 수 없습니다.");
//        }
//
//        return locations;
//    }
//}
//
