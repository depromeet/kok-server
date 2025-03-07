package com.kok.kokapi.station.adapter.out.persistence;

import com.kok.kokcore.station.application.port.out.SaveRoutePort;
import com.kok.kokcore.station.domain.entity.Route;
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
public class RoutePersistenceAdapter implements SaveRoutePort {
    private static final String INSERT_ROUTE_SQL = """
            INSERT INTO route (code, name, station_id)
            VALUES(?, ?, ?)
        """;

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void saveRoutes(List<Route> routes) {
        if (routes.isEmpty()) {
            log.info("No routes to save.");
            return;
        }
        batchInsertRoutes(routes);
    }

    private void batchInsertRoutes(List<Route> routes) {
        int[] batches = jdbcTemplate.batchUpdate(INSERT_ROUTE_SQL,
            new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    Route route = routes.get(i);
                    ps.setLong(1, route.getCode());
                    ps.setString(2, route.getName());
                    ps.setLong(3, route.getStation().getId());
                }

                @Override
                public int getBatchSize() {
                    return routes.size();
                }
            });
        log.info("Successfully saved a total of {} routes out of {}.",
            Arrays.stream(batches).sum(), routes.size());
    }
}
