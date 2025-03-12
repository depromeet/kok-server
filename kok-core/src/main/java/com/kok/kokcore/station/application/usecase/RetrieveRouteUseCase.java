package com.kok.kokcore.station.application.usecase;

import com.kok.kokcore.station.domain.entity.Route;
import com.kok.kokcore.station.domain.entity.Station;

import java.util.List;

public interface RetrieveRouteUseCase {
    List<Route> retrieveRoutes(Station station);
}
