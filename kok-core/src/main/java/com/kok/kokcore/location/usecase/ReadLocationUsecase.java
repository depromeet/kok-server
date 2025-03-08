package com.kok.kokcore.location.usecase;

import com.kok.kokcore.location.domain.Location;

import java.util.List;

public interface ReadLocationUsecase {
    Location readLocation(String uuid, Integer memberId);
    List<Location> readLocations(String uuid);
    List<Location> readInsideConvexHull(String uuid);
    List<Location> readConvexHull(String uuid);
}
