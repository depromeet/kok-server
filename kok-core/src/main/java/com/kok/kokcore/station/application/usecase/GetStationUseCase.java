package com.kok.kokcore.station.application.usecase;

import com.kok.kokcore.station.domain.entity.Station;

public interface GetStationUseCase {

    Station getStation(long stationId);
}
