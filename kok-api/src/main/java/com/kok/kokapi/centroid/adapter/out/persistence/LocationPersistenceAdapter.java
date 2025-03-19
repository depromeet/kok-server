package com.kok.kokapi.centroid.adapter.out.persistence;


import com.kok.kokcore.location.domain.Location;
import com.kok.kokcore.location.application.port.out.ReadCentroidPort;
import com.kok.kokcore.location.application.port.out.ReadLocationPort;
import com.kok.kokcore.location.application.port.out.SaveLocationPort;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Repository;
import org.locationtech.jts.io.WKTReader;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LocationPersistenceAdapter implements ReadCentroidPort, SaveLocationPort, ReadLocationPort {

    private final LocationRepository locationRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<Location> findLocationByRoomIdAndMemberId(String roomId, String memberId) {
        return locationRepository.findLocationByRoomIdAndMemberId(roomId, memberId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Location> findLocationsByRoomId(String roomId) {
        return locationRepository.findLocationsByRoomId(roomId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Location> findInsideConvexHull(String roomId) {
        return locationRepository.findInsideConvexHull(roomId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Location> findConvexHull(String roomId) {
        return locationRepository.findConvexHull(roomId);
    }

    @Override
    @Transactional(readOnly = true)
    public Point findCentroidByRoomId(String roomId) {
        String centroidWKT = locationRepository.findCentroidByRoomId(roomId); // WKT 형식으로 받음
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
    public Location saveLocation(String roomId, String memberId, Point point) {
        return locationRepository.save(new Location(roomId, memberId, point));
    }
}
