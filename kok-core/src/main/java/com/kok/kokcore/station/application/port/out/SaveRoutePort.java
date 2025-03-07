package com.kok.kokcore.station.application.port.out;

import com.kok.kokcore.station.domain.entity.Route;
import java.util.List;

public interface SaveRoutePort {

    void saveRoutes(List<Route> routes);
}
