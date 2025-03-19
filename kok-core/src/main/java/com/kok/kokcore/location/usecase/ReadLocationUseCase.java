package com.kok.kokcore.location.usecase;

import com.kok.kokcore.location.domain.Location;

import java.util.List;

public interface ReadLocationUseCase {
    Location readLocation(String roomId, String memberId);
    List<Location> readLocations(String roomId);
    List<Location> readInsideConvexHull(String roomId);
    List<Location> readConvexHull(String roomId);
}
