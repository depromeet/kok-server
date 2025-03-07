package com.kok.kokapi.station.application.service;

import com.kok.kokcore.station.application.port.out.LoadStationsPort;
import com.kok.kokcore.station.application.port.out.ReadStationsPort;
import com.kok.kokcore.station.application.port.out.SaveRoutePort;
import com.kok.kokcore.station.application.port.out.SaveStationsPort;
import com.kok.kokcore.station.application.port.out.dto.StationRouteDtos;
import com.kok.kokcore.station.application.usecase.SaveStationUseCase;
import com.kok.kokcore.station.domain.entity.Station;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StationService implements SaveStationUseCase {

    private final LoadStationsPort loadStationsPort;
    private final SaveStationsPort saveStationsPort;
    private final ReadStationsPort readStationsPort;
    private final SaveRoutePort saveRoutePort;

    @Override
    @Transactional
    public void saveStations() {
        if(readStationsPort.hasNoStations()) {
            StationRouteDtos stationRouteDtos = loadStationsPort.loadAllStations();
            List<Station> stations = saveStationsPort.saveStations(stationRouteDtos.toStations());
            saveRoutePort.saveRoutes(stationRouteDtos.toRoutesByStations(stations));
        }
    }
}
