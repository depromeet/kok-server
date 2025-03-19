package com.kok.kokcore.location.application.port.out;

import com.kok.kokcore.location.domain.Location;

import java.util.List;
import java.util.Optional;

public interface ReadLocationPort {
    Optional<Location> findLocationByRoomIdAndMemberId(String roomId, String memberId);
    List<Location> findLocationsByRoomId(String roomId);
    List<Location> findInsideConvexHull(String roomId);
    List<Location> findConvexHull(String roomId);
}
