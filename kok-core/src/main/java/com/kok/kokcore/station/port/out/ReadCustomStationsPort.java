package com.kok.kokcore.station.port.out;

import com.kok.kokcore.station.domain.entity.Station;
import java.util.List;

public interface ReadCustomStationsPort {
    List<Station> findRecommendedStationsByRoomId(String roomId);
}
