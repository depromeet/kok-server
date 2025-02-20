package com.kok.kokcore.application.port.outbound;

import com.kok.kokcore.application.domain.entity.Location;

import java.util.List;
import java.util.Optional;

public interface ReadLocationPort {
    Optional<Location> findLocationByUuidAndMemberId(String uuid, Integer memberId);
    List<Location> findLocationsByUuid(String uuid);
}
