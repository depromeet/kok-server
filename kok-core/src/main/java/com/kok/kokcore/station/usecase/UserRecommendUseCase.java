package com.kok.kokcore.station.usecase;

import com.kok.kokcore.station.domain.entity.Station;
import java.util.List;

public interface UserRecommendUseCase {

    Station addUserRecommendStation(String roomId, Long stationId);

    List<Station> getUserRecommendStation(String roomId);

    List<Station> searchStations(String keyword);

}
