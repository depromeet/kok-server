package com.kok.kokapi.station.adapter.out.persistence;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.kok.kokapi.common.template.RepositoryTest;
import com.kok.kokcore.station.domain.entity.Station;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class StationRepositoryTest extends RepositoryTest {

    @Autowired
    private StationRepository stationRepository;

    @DisplayName("데이터가 하나라도 존재한다면, 참을 반환한다.")
    @Test
    void existsAny() {
        // given
        stationRepository.save(new Station("서울역", BigDecimal.ONE, BigDecimal.ONE, 0));

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
