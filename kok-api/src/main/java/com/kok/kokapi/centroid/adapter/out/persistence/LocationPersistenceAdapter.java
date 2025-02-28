package com.kok.kokapi.centroid.adapter.out.persistence;


import com.kok.kokcore.location.domain.Location;
import com.kok.kokcore.location.application.port.out.ReadCentroidPort;
import com.kok.kokcore.location.application.port.out.ReadLocationPort;
import com.kok.kokcore.location.application.port.out.SaveLocationPort;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Repository;
import org.locationtech.jts.io.WKTReader;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LocationPersistenceAdapter implements ReadCentroidPort, SaveLocationPort, ReadLocationPort {
    private final LocationRepository locationRepository;

    @Override
    public Optional<Location> findLocationByUuidAndMemberId(String uuid, Integer memberId) {
        return locationRepository.findLocationByUuidAndMemberId(uuid, memberId);
    }

    @Override
    public List<Location> findLocationsByUuid(String uuid) {
        return locationRepository.findLocationsByUuid(uuid);
    }

    @Override
    public Point findCentroidByUuid(String uuid) {
        String centroidWKT = locationRepository.findCentroidByUuid(uuid); // WKT 형식으로 받음
        if (centroidWKT == null) {
            return null;
        }

        try {
            WKTReader reader = new WKTReader();
            return (Point) reader.read(centroidWKT);
        } catch (Exception e) {
            throw new RuntimeException("파싱 실패..", e);
        }
    }

    @Override
    public Location saveLocation(String uuid, Integer memberId, Point point) {
        return locationRepository.save(new Location(uuid, memberId, point));
    }
}
