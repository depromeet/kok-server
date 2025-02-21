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

    private final StationRepository stationRepository;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void saveStations(List<Station> stations) {
        if (stations.isEmpty()) {
            log.info("No stations to save.");
            return;
        }
        int[] batches = jdbcTemplate.batchUpdate(INSERT_SQL, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Station station = stations.get(i);
                ps.setLong(1, station.getStationId());
                ps.setString(2, station.getName());
                ps.setString(3, station.getRoute());
                ps.setBigDecimal(4, station.getLatitude());
                ps.setBigDecimal(5, station.getLongitude());
                ps.setLong(6, station.getPriority());
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
