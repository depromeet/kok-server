package com.kok.kokapi.station.adapter.out.persistence;

import com.kok.kokcore.station.application.port.out.ReadStationsPort;
import com.kok.kokcore.station.application.port.out.SaveStationsPort;
import com.kok.kokcore.station.domain.entity.Station;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class StationPersistenceAdapter implements SaveStationsPort, ReadStationsPort {

    private static final String INSERT_SQL = """
            INSERT INTO station (station_id, name, route, latitude, longitude, priority)
            VALUES (?, ?, ?, ?, ?, ?)
        """;
    private static final String INSERT_ROUTE_SQL = """
            INSERT INTO ROUTE (code, route, station_id)
            VALUES(?, ?, ?)
        """;

    private final StationRepository stationRepository;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void saveStations(StationRouteDtos stationRouteDtos) {
        if (stationRouteDtos.isEmpty()) {
            log.info("No stations to save.");
            return;
        }
        batchInsertStations(stationRouteDtos);
    }

    private void batchInsertStations(StationRouteDtos stationRouteDtos) {
        List<Station> stations = stationRouteDtos.toStations();
        int[] batches = jdbcTemplate.batchUpdate(INSERT_STATION_SQL,
            new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    Station station = stations.get(i);
                    ps.setString(1, station.getName());
                    ps.setBigDecimal(2, station.getLatitude());
                    ps.setBigDecimal(3, station.getLongitude());
                    ps.setLong(4, station.getPriority());
                }

                @Override
                public int getBatchSize() {
                    return stations.size();
                }
            });
        log.info("Successfully saved a total of {} stations out of {}.",
            Arrays.stream(batches).sum(), stations.size());
    }

    @Override
    public boolean hasNoStations() {
        return !stationRepository.existsAny();
    }
}
