package com.kok.kokapi.station.application.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.kok.kokapi.common.config.ServiceTestConfig;
import com.kok.kokapi.common.util.DatabaseCleanerExtension;
import com.kok.kokapi.station.adapter.outbound.persistence.StationRepository;
import com.kok.kokcore.station.domain.entity.Station;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@ExtendWith(DatabaseCleanerExtension.class)
@Import({ServiceTestConfig.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class StationServiceTest {

    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private StationService stationService;

    @DisplayName("저장된 지하철 정보가 없다면, 지하철 정보(총 2개)를 불러와서 저장한다.")
    @Test
    void saveStations() {
        // when
        stationService.saveStations();

        // then
        assertThat(stationRepository.findAll()).hasSize(2);
    }

    @DisplayName("이미 지하철 정보가 저장되어 있다면, 저장하지 않는다.")
    @Test
    void doesNotSaveStationsIfAlreadyExists() {
        //given
        stationRepository.save(new Station(1, "서울역", "1호선", BigDecimal.ONE, BigDecimal.ONE));

        // when
        stationService.saveStations();

        // then
        assertThat(stationRepository.findAll()).hasSize(1);
    }
}
