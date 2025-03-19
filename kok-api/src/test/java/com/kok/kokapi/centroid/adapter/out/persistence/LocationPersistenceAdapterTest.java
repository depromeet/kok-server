package com.kok.kokapi.centroid.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import com.kok.kokapi.common.template.RepositoryTest;
import com.kok.kokapi.fixture.PointFixture;
import com.kok.kokcore.location.domain.Location;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class LocationPersistenceAdapterTest extends RepositoryTest {

    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private LocationPersistenceAdapter locationPersistenceAdapter;

    @DisplayName("현재 약속방의 모든 인원이 출발지를 입력했다.")
    @Test
    void existsAllByMemberIds() {
        // given
        String memberId = "memberId";
        String memberId2 = "memberId2";
        locationRepository.save(new Location("roomId", memberId, PointFixture.create()));
        locationRepository.save(new Location("roomId", memberId2, PointFixture.create()));

        // when
        boolean result = locationPersistenceAdapter.existsAllByMemberIds(List.of(memberId, memberId2));

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("현재 약속방의 모든 인원이 출발지를 입력하지 않았으면 false를 반환한다.")
    @Test
    void notExistsAllByMemberIds() {
        // given
        String memberId = "memberId";
        String memberId2 = "memberId2";
        locationRepository.save(new Location("roomId", memberId, PointFixture.create()));

        // when
        boolean result = locationPersistenceAdapter.existsAllByMemberIds(List.of(memberId, memberId2));

        // then
        assertThat(result).isFalse();
    }
}