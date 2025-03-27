package com.kok.kokcore.station.usecase;

import com.kok.kokcore.station.domain.entity.Station;
import java.util.List;

public interface CustomStationUseCase {

    Station addCustomStations(String roomId, Long stationId);

    List<Station> getCustomRecommendedStations(String roomId);

    List<Station> searchStations(String keyword);

}
