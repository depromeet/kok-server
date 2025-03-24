package com.kok.kokcore.station.application.usecase;

import com.kok.kokcore.station.domain.entity.Station;
import java.util.List;

public interface GetRecommendStationUseCase {

    List<Station> getRecommendedStations(String roomId);
}
