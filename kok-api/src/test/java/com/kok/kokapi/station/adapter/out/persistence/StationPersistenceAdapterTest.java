package com.kok.kokapi.station.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.kok.kokapi.common.template.ServiceTest;
import com.kok.kokcore.station.domain.entity.Station;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class StationPersistenceAdapterTest extends ServiceTest {

    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private StationPersistenceAdapter stationPersistenceAdapter;

    @DisplayName("새로 저장된 지하철 목록만 반환한다.")
    @Test
    void saveStationsAndReturn() {
        // given
        Station savedStation = new Station("망원역", "12.345","123.456");
        List<Station> stations = List.of(new Station("합정역", "12.345","123.456"));
        stationRepository.save(savedStation);

        // when
        List<Station> result = stationPersistenceAdapter.saveStations(stations);

        // then
        assertAll(
            () -> assertThat(result).hasSize(1),
            () -> assertThat(result.get(0).getName()).isEqualTo("합정역")
        );
    }
}