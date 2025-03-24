package com.kok.kokapi.station.application.service;

import com.kok.kokcore.station.domain.entity.Route;
import com.kok.kokcore.station.domain.entity.Station;
import com.kok.kokcore.station.port.out.RetrieveRoutePort;
import com.kok.kokcore.station.usecase.RetrieveRouteUseCase;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RouteService implements RetrieveRouteUseCase {

    private final RetrieveRoutePort retrieveRoutePort;

    @Override
    public List<Route> retrieveRoutes(Station station) {
        return retrieveRoutePort.retrieveRoutes(station);
    }
}
