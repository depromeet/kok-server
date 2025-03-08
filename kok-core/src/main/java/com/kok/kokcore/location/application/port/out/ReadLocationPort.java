package com.kok.kokcore.location.application.port.out;

import com.kok.kokcore.location.domain.Location;

import java.util.List;
import java.util.Optional;

public interface ReadLocationPort {
    Optional<Location> findLocationByUuidAndMemberId(String uuid, Integer memberId);
    List<Location> findLocationsByUuid(String uuid);
    List<Location> findInsideConvexHull(String uuid);
    List<Location> findConvexHull(String uuid);
}
