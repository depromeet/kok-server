package com.kok.kokcore.station.application.port.out;

import com.kok.kokcore.station.application.port.out.dto.StationRouteDtos;

public interface LoadStationsPort {

    StationRouteDtos loadAllStations();
}
