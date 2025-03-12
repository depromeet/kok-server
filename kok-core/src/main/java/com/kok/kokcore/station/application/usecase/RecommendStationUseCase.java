package com.kok.kokcore.station.application.usecase;

import com.kok.kokcore.station.domain.entity.Station;
import org.locationtech.jts.geom.Point;

import java.util.List;

public interface RecommendStationUseCase {

    List<Station> recommendStations(Point centroid);
}
