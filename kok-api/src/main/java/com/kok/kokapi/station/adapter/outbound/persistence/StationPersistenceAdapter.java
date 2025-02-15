package com.kok.kokapi.station.adapter.outbound.persistence;

import com.kok.kokcore.station.application.port.outbound.SaveStationsPort;
import com.kok.kokcore.station.domain.entity.Station;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StationPersistenceAdapter implements SaveStationsPort {

    private final StationRepository stationRepository;

    @Override
    public void saveStations(List<Station> stations) {
        stationRepository.saveAll(stations);
    }
}
