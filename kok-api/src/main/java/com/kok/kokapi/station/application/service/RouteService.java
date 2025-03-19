package com.kok.kokapi.station.application.service;

import com.kok.kokcore.station.application.port.out.RetrieveRoutePort;
import com.kok.kokcore.station.application.usecase.RetrieveRouteUseCase;
import com.kok.kokcore.station.domain.entity.Route;
import com.kok.kokcore.station.domain.entity.Station;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RouteService implements RetrieveRouteUseCase {

    private final RetrieveRoutePort retrieveRoutePort;

    @Override
    public List<Route> retrieveRoutes(Station station) {
        return retrieveRoutePort.retrieveRoutes(station);
    }
}
