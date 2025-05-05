package com.kok.kokcore.station.port.out;

import com.kok.kokcore.station.domain.entity.Route;
import java.util.List;

public interface SaveRoutePort {

    void saveRoutes(List<Route> routes);
}
