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

    private static final String INSERT_STATION_SQL = """
            INSERT INTO station (name, latitude, longitude, priority)
            VALUES (?, ?, ?, ?)
        """;

    private final StationRepository stationRepository;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Station> saveStations(List<Station> stations) {
        if (stations.isEmpty()) {
            log.info("No stations to save.");
            return List.of();
        }
        return batchInsertStations(stations);
    }

    private List<Station> batchInsertStations(List<Station> stations) {
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
        return stationRepository.findAll();
    }

    @Override
    public boolean hasNoStations() {
        return !stationRepository.existsAny();
    }
}
