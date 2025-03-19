package com.kok.kokapi.centroid.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import com.kok.kokapi.common.template.RepositoryTest;
import com.kok.kokapi.fixture.PointFixture;
import com.kok.kokcore.location.domain.Location;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class LocationPersistenceAdapterTest extends RepositoryTest {

    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private LocationPersistenceAdapter locationPersistenceAdapter;

    @DisplayName("현재 약속방에 출발지 입력 완료한 사용자 수를 조회한다..")
    @Test
    void countByRoomId() {
        // given
        String memberId = "memberId";
        String memberId2 = "memberId2";
        String roomId = "roomId";
        locationRepository.save(new Location(roomId, memberId, PointFixture.create()));
        locationRepository.save(new Location(roomId, memberId2, PointFixture.create()));

        // when
        long count = locationPersistenceAdapter.countByRoomId(roomId);

        // then
        assertThat(count).isEqualTo(2);
    }
}