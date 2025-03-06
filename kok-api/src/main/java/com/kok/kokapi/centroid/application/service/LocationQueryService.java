package com.kok.kokapi.centroid.application.service;

import com.kok.kokapi.config.geometry.PointConverter;
import com.kok.kokcore.location.domain.Location;
import com.kok.kokcore.location.application.port.out.ReadLocationPort;
import com.kok.kokcore.location.usecase.ReadLocationUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationQueryService implements ReadLocationUsecase{

    private final ReadLocationPort readLocationPort;
    private final PointConverter pointConverter;

    @Override
    public Location readLocation(String uuid, Integer memberId) {
        return readLocationPort.findLocationByUuidAndMemberId(uuid, memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버의 위치를 찾을 수 없습니다."));
    }

    @Override
    public List<Location> readLocations(String uuid) {
        return readLocationPort.findLocationsByUuid(uuid);
    }

    @Override
    public List<Location> readInsideConvexHull(String uuid) {
        return readLocationPort.findInsideConvexHull(uuid);
    }

    @Override
    public List<Location> readConvexHull(String uuid) {
        return readLocationPort.findConvexHull(uuid);
    }
}
