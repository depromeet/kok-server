package com.kok.kokcore.station.application.port.outbound;

import com.kok.kokcore.station.domain.entity.Station;
import java.util.List;

public interface LoadStationsPort {

    List<Station> loadAllStations();
}
