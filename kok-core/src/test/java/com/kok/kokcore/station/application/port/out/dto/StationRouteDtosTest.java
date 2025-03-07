package com.kok.kokcore.station.application.port.out.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.kok.kokcore.station.domain.entity.Route;
import com.kok.kokcore.station.domain.entity.Station;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StationRouteDtosTest {

    @DisplayName("stationRouteDtos가 비어 있으면 참을 반환한다.")
    @Test
    void isEmpty() {
        // given
        StationRouteDtos stationRouteDtos = new StationRouteDtos(List.of());

        // when
        boolean result = stationRouteDtos.isEmpty();

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("stationRouteDtos가 비어 있지 않으면 거짓을 반환한다.")
    @Test
    void isNotEmpty() {
        // given
        StationRouteDto stationRouteDto = new StationRouteDto("서울역", "37.556", "126.972", 1L,
            "1호선");
        StationRouteDtos stationRouteDtos = new StationRouteDtos(List.of(stationRouteDto));

        // when
        boolean result = stationRouteDtos.isEmpty();

        // then
        assertThat(result).isFalse();
    }

    @Disabled
    @DisplayName("stationRouteDtos를 Station 리스트로 중복 없이 변환한다.")
    @Test
    void toStations() {
        // given
        StationRouteDto stationRouteDto1 = new StationRouteDto("서울역", "37.556", "126.972", 1L,
            "1호선");
        StationRouteDto stationRouteDto2 = new StationRouteDto("강남역", "37.497", "127.028", 2L,
            "2호선");
        StationRouteDto stationRouteDto3 = new StationRouteDto("강남역", "37.496", "127.029", 3L, "신분당선");
        StationRouteDtos stationRouteDtos = new StationRouteDtos(
            List.of(stationRouteDto1, stationRouteDto2, stationRouteDto3)
        );

        // when
        List<Station> stations = stationRouteDtos.toStations();

        // then
        List<String> names = stations.stream().map(Station::getName).toList();

        assertAll(
            () -> assertThat(stations).hasSize(2),
            () -> assertThat(names).containsExactlyInAnyOrder("서울역", "강남역")
        );
    }

    @DisplayName("stationRouteDtos를 Route 리스트로 변환한다.")
    @Test
    void toRoutesByStations() {
        // given
        StationRouteDto stationRouteDto1 = new StationRouteDto("서울역", "37.556", "126.972", 1L,
            "1호선");
        StationRouteDto stationRouteDto2 = new StationRouteDto("강남역", "37.497", "127.028", 2L,
            "2호선");
        StationRouteDto stationRouteDto3 = new StationRouteDto("강남역", "37.497", "127.028", 3L, "신분당선");
        StationRouteDtos stationRouteDtos = new StationRouteDtos(
            List.of(stationRouteDto1, stationRouteDto2, stationRouteDto3)
        );
        Station station1 = new Station("서울역", "37.556", "126.972");
        Station station2 = new Station("강남역", "37.497", "127.028");

        // when
        List<Route> routes = stationRouteDtos.toRoutesByStations(List.of(station1, station2));

        // then
        List<Long> codes = routes.stream().map(Route::getCode).toList();
        List<Station> stations = routes.stream().map(Route::getStation).distinct().toList();

        assertAll(
            () -> assertThat(routes).hasSize(3),
            () -> assertThat(codes).containsExactlyInAnyOrder(1L, 2L, 3L),
            () -> assertThat(stations).containsExactlyInAnyOrder(station1, station2)
        );
    }
}