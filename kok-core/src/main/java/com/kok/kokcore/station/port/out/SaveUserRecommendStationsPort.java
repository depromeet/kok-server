package com.kok.kokcore.station.port.out;

import com.kok.kokcore.station.domain.entity.Station;

public interface SaveUserRecommendStationsPort {

    Station addUserRecommendStation(String roomId, Station station);
}
