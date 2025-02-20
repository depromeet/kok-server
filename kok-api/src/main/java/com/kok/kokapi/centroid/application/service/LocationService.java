package com.kok.kokapi.centroid.application.service;

import com.kok.kokapi.centroid.adapter.outbound.persistence.LocationPersistenceAdapter;
import com.kok.kokcore.application.domain.entity.Location;
import com.kok.kokcore.application.usecase.CreateLocationUsecase;
import com.kok.kokcore.application.usecase.ReadCentroidUsecase;
import com.kok.kokcore.application.usecase.ReadLocationUsecase;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class LocationService implements CreateLocationUsecase, ReadCentroidUsecase, ReadLocationUsecase {

    private final LocationPersistenceAdapter locationRepository;

    @Override
    public Location createLocation(String uuid, Integer memberId, Point point) {
        return locationRepository.saveLocation(uuid, memberId, point);
    }

    @Override
    @Transactional
    public Location UpdateLocation(String uuid, Integer memberId, Point point) {
        Location location = locationRepository.findLocationByUuidAndMemberId(uuid, memberId).orElseThrow(
                () -> new RuntimeException("해당 Id의 정보가 없습니다.")
        );
        location.changePoint(point);
        return location;
    }

    @Override
    public Point readCentroid(String uuid) {
        return locationRepository.findCentroidByUuid(uuid);
    }

    @Override
    public Location readLocation(String uuid, Integer memberId) {
        return locationRepository.findLocationByUuidAndMemberId(uuid, memberId).orElse(null);
    }

    @Override
    public List<Location> readLocations(String uuid) {
        return locationRepository.findLocationsByUuid(uuid);
    }
}
