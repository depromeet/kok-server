package com.kok.kokcore.station.port.out;

import com.kok.kokcore.station.domain.entity.Route;
import com.kok.kokcore.station.domain.entity.Station;

import java.util.List;

public interface RetrieveRoutePort {

    List<Route> retrieveRoutes(Station station);
}
