package com.kok.kokapi.station.adapter.out.persistence;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.kok.kokapi.common.config.DataJpaTestConfig;
import com.kok.kokcore.station.domain.entity.Station;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({DataJpaTestConfig.class})
class StationRepositoryTest {

    @Autowired
    private StationRepository stationRepository;

    @DisplayName("데이터가 하나라도 존재한다면, 참을 반환한다.")
    @Test
    void existsAny() {
        // given
        stationRepository.save(new Station(1, "서울역", "1호선", BigDecimal.ONE, BigDecimal.ONE));

        // when
        boolean result = stationRepository.existsAny();

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("데이터가 하나도 존재하지 않는다면, 거짓을 반환한다.")
    @Test
    void notExistsAny() {
        // when
        boolean result = stationRepository.existsAny();

        // then
        assertThat(result).isFalse();
    }
}
