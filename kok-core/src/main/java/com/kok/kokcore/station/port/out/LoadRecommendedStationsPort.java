package com.kok.kokcore.station.port.out;

import com.kok.kokcore.station.domain.entity.Station;
import java.util.List;

public interface LoadRecommendedStationsPort {

    List<Station> getStationsByRoomId(String roomId);
}
