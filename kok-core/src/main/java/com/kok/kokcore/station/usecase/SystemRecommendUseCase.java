package com.kok.kokcore.station.usecase;

import com.kok.kokcore.station.domain.entity.Station;
import java.util.List;


public interface SystemRecommendUseCase {

    List<Station> systemRecommendStation(String roomId);
}
