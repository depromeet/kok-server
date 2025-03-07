package com.kok.kokapi.station.adapter.out.persistence;

import com.kok.kokcore.station.application.port.out.SaveRoutePort;
import com.kok.kokcore.station.domain.entity.Route;
import java.util.List;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class RoutePersistenceAdapter implements SaveRoutePort {

    private static final String INSERT_ROUTE_SQL = """
            INSERT INTO route (code, name, station_id)
            VALUES (:code, :name, :station_id)
        """;
    private static final Function<Route, MapSqlParameterSource> mapToParams = route ->
        new MapSqlParameterSource()
            .addValue("code", route.getCode())
            .addValue("name", route.getName())
            .addValue("station_id", route.getStation().getId());

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public void saveRoutes(List<Route> routes) {
        if (routes.isEmpty()) {
            log.info("No routes to save.");
            return;
        }
        batchInsertRoutes(routes);
    }

    private void batchInsertRoutes(List<Route> routes) {
        int savedCount = 0;
        for (Route route : routes) {
            MapSqlParameterSource parameters = mapToParams.apply(route);
            KeyHolder keyHolder = new GeneratedKeyHolder();
            int updateCount = jdbcTemplate.update(INSERT_ROUTE_SQL, parameters, keyHolder,
                new String[]{"id"});
            if (updateCount > 0) {
                savedCount++;
            }
        }
        log.info("Successfully saved a total of {} routes out of {}.", savedCount, routes.size());
    }
}
