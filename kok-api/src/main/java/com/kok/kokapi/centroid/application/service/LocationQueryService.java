package com.kok.kokapi.centroid.application.service;

import com.kok.kokapi.centroid.adapter.out.persistence.LocationPersistenceAdapter;
import com.kok.kokcore.application.domain.entity.Location;
import com.kok.kokcore.application.usecase.ReadLocationUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationQueryService implements ReadLocationUsecase{

    private final LocationPersistenceAdapter locationRepository;

    @Override
    public Location readLocation(String uuid, Integer memberId) {
        return locationRepository.findLocationByUuidAndMemberId(uuid, memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버의 위치를 찾을 수 없습니다."));
    }

    @Override
    public List<Location> readLocations(String uuid) {
        List<Location> locations = locationRepository.findLocationsByUuid(uuid);
        if (locations.isEmpty()) {
            throw new IllegalArgumentException("해당 UUID에 대한 위치들을 찾을 수 없습니다.");
        }
        return locations;
    }
}
