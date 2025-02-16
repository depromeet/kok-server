package com.kok.kokapi.station.application.service;

import com.kok.kokcore.station.application.port.outbound.LoadStationsPort;
import com.kok.kokcore.station.application.port.outbound.ReadStationsPort;
import com.kok.kokcore.station.application.port.outbound.SaveStationsPort;
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

    @Override
    @Transactional
    public void saveStations() {
        if(hasNoStations()) {
            List<Station> stations = loadStationsPort.loadAllStations();
            saveStationsPort.saveStations(stations);
        }
    }

    private boolean hasNoStations() {
        return readStationsPort.hasNoStations();
    }
}
