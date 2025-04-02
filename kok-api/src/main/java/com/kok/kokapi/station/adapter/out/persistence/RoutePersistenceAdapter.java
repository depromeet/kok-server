package com.kok.kokapi.station.adapter.out.persistence;

import com.kok.kokcore.station.domain.entity.Route;
import com.kok.kokcore.station.domain.entity.Station;
import com.kok.kokcore.station.port.out.RetrieveRoutePort;
import com.kok.kokcore.station.port.out.SaveRoutePort;
import java.util.List;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Slf4j
@RequiredArgsConstructor
public class RoutePersistenceAdapter implements SaveRoutePort, RetrieveRoutePort {

    public static final List<String> ROUTE_ONE_LIST = List.of("경부선", "경인선", "경원선", "장항선");
    private final RouteRepository routeRepository;

    private static final String INSERT_ROUTE_SQL = """
            INSERT INTO route (name, station_id)
            VALUES (:name, :station_id)
        """;
    private static final Function<Route, MapSqlParameterSource> mapToParams = route ->
        new MapSqlParameterSource()
            .addValue("name", route.getName())
            .addValue("station_id", route.getStation().getId());

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public void saveRoutes(List<Route> routes) {
        if (routes.isEmpty()) {
            log.debug("No routes to save.");
            return;
        }
        batchInsertRoutes(routes);
        int updatedCount = routeRepository.updateRouteNameToRouteOne(ROUTE_ONE_LIST);
        log.debug("Successfully changed {} route name to \"1호선\".", updatedCount);
    }

    private void batchInsertRoutes(List<Route> routes) {
        MapSqlParameterSource[] batchParams = routes.stream()
            .map(mapToParams)
            .toArray(MapSqlParameterSource[]::new);
        int[] batched = jdbcTemplate.batchUpdate(INSERT_ROUTE_SQL, batchParams);
        log.debug("Successfully saved a total of {} routes out of {}.", batched.length,
            routes.size());
    }


    @Override
    @Transactional(readOnly = true)
    public List<Route> retrieveRoutes(Station station) {
        return routeRepository.findAllByStationOrderByName(station);
    }
}
