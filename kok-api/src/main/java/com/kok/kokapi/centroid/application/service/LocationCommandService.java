package com.kok.kokapi.centroid.application.service;

import com.kok.kokapi.centroid.adapter.out.persistence.LocationPersistenceAdapter;
import com.kok.kokapi.config.geometry.PointConverter;
import com.kok.kokcore.application.domain.entity.Location;
import com.kok.kokcore.application.usecase.CreateLocationUsecase;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class LocationCommandService implements CreateLocationUsecase {

    private final LocationPersistenceAdapter locationRepository;
    private final PointConverter pointConverter;

    @Override
    public Location createLocation(String uuid, Integer memberId, BigDecimal latitude, BigDecimal longitude) {
        Point point = pointConverter.fromCoordinates(latitude, longitude);
        return locationRepository.saveLocation(uuid, memberId, point);
    }

    @Override
    @Transactional
    public Location updateLocation(String uuid, Integer memberId, BigDecimal latitude, BigDecimal longitude) {
        Location location = locationRepository.findLocationByUuidAndMemberId(uuid, memberId)
                .orElseThrow(() -> new RuntimeException("해당 ID의 정보가 없습니다."));
        Point newPoint = pointConverter.fromCoordinates(latitude, longitude);
        location.changePoint(newPoint);
        return location;
    }
}
