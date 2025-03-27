package com.kok.kokcore.station.application.port.out;

import com.kok.kokcore.station.domain.entity.Station;

public interface SaveCustomStationsPort {

    Station addCustomStations(String roomId, Station station);
}
