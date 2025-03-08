package com.kok.kokcore.station.application.port.out;

import com.kok.kokcore.station.domain.entity.Station;

import java.util.Optional;

public interface RetrieveStationsPort {
    Optional<Station> retrieveStation(Long stationId);
}
