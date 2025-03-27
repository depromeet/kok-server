package com.kok.kokcore.station.usecase;

import com.kok.kokcore.station.domain.entity.Station;
import java.util.List;


public interface RecommendStationUseCase {

    List<Station> recommendStations(String roomId);

}
