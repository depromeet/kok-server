package com.kok.kokcore.application.usecase;

import com.kok.kokcore.application.domain.entity.Location;

import java.util.List;

public interface ReadLocationUsecase {
    Location readLocation(String uuid, Integer memberId);
    List<Location> readLocations(String uuid);
}
