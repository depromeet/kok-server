package com.kok.kokapi.station.adapter.out.persistence;

import com.kok.kokcore.station.application.port.out.ReadStationsPort;
import com.kok.kokcore.station.application.port.out.SaveStationsPort;
import com.kok.kokcore.station.domain.entity.Station;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
@RequiredArgsConstructor
public class StationPersistenceAdapter implements SaveStationsPort, ReadStationsPort {

    private static final String INSERT_STATION_SQL = """
            INSERT INTO station (name, latitude, longitude, priority)
            VALUES (:name, :latitude, :longitude, :priority)
        """;
    private static final Function<Station, MapSqlParameterSource> mapToParams = station ->
        new MapSqlParameterSource()
            .addValue("name", station.getName())
            .addValue("latitude", station.getLatitude())
            .addValue("longitude", station.getLongitude())
            .addValue("priority", station.getPriority());

    private final StationRepository stationRepository;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<Station> saveStations(List<Station> stations) {
        if (stations.isEmpty()) {
            log.debug("No stations to save.");
            return List.of();
        }
        return batchInsertStations(stations);
    }

    private List<Station> batchInsertStations(List<Station> stations) {
        List<Long> savedStationIds = new ArrayList<>();
        for (Station station : stations) {
            MapSqlParameterSource parameters = mapToParams.apply(station);
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(INSERT_STATION_SQL, parameters, keyHolder, new String[]{"id"});
            if(Objects.nonNull(keyHolder.getKey())){
                savedStationIds.add(keyHolder.getKey().longValue());
            }
        }
        log.debug("Successfully saved a total of {} stations out of {}.", savedStationIds.size(), stations.size());
        return stationRepository.findAllById(savedStationIds);
    }

    @Override
    public boolean hasNoStations() {
        return !stationRepository.existsAny();
    }
}

