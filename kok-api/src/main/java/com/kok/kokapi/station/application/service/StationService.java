package com.kok.kokapi.station.application.service;

import com.kok.kokcore.station.application.port.outbound.LoadStationsPort;
import com.kok.kokcore.station.application.port.outbound.SaveStationsPort;
import com.kok.kokcore.station.application.usecase.SaveStationUseCase;
import com.kok.kokcore.station.domain.entity.Station;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StationService implements SaveStationUseCase {

    private final LoadStationsPort loadStationsPort;
    private final SaveStationsPort saveStationsPort;

    @Override
    public void saveStations() {
        List<Station> stations = loadStationsPort.loadAllStations();
        saveStationsPort.saveStations(stations);
    }
}
