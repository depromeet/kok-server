package com.kok.kokapi.station.adapter.out.external;

import com.kok.kokcore.station.application.port.out.LoadStationsPort;
import com.kok.kokcore.station.domain.entity.Station;
import java.math.BigDecimal;
import java.util.List;

public class FakeStationClient implements LoadStationsPort {

    @Override
    public List<Station> loadAllStations() {
        return List.of(
            new Station(1, "서울역", "1호선", BigDecimal.ONE, BigDecimal.ONE),
            new Station(2, "합정역", "2호선", BigDecimal.TEN, BigDecimal.TEN)
        );
    }
}
