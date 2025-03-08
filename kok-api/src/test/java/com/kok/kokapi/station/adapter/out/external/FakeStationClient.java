package com.kok.kokapi.station.adapter.out.external;

import com.kok.kokcore.station.application.port.out.LoadStationsPort;
import com.kok.kokcore.station.application.port.out.dto.StationRouteDto;
import com.kok.kokcore.station.application.port.out.dto.StationRouteDtos;
import java.util.List;

public class FakeStationClient implements LoadStationsPort {

    @Override
    public StationRouteDtos loadAllStations() {
        return new StationRouteDtos(
            List.of(
                new StationRouteDto("서울역", "1", "2", 1L, "1호선"),
                new StationRouteDto("합정역", "10", "10", 2L, "2호선")
            )
        );
    }
}
