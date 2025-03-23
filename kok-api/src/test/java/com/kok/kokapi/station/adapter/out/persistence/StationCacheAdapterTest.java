package com.kok.kokapi.station.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.kok.kokapi.common.template.RepositoryTest;
import com.kok.kokcore.station.domain.entity.Station;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.redis.core.RedisTemplate;

class StationCacheAdapterTest extends RepositoryTest {

    @Autowired
    private StationCacheAdapter stationCacheAdapter;
    @Autowired
    @Qualifier("stationCacheManager")
    private CacheManager cacheManager;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @DisplayName("roomId로 추천된 지하철역을 캐시에서 조회한다.")
    @Test
    void getStationsByRoomId() {
        // given
        String roomId = "room1";
        List<Station> stations = List.of(
            new Station("잠실역", "37.511", "127.1"),
            new Station("강남역", "37.497", "127.02")
        );
        cacheManager.getCache(StationCacheAdapter.RECOMMEND_STATIONS_CACHE_KEY)
            .put(roomId, stations);

        // when
        List<Station> result = stationCacheAdapter.getStationsByRoomId(roomId);

        // then
        assertThat(result).containsExactlyInAnyOrder(stations.get(0), stations.get(1));
    }

    @DisplayName("roomId에 해당하는 캐시가 없으면 예외를 발생시킨다.")
    @Test
    void cannotGetStationsByRoomId() {
        // given
        String roomId = "nonexistentRoom";

        // when & then
        assertThatThrownBy(() -> stationCacheAdapter.getStationsByRoomId(roomId))
            .isInstanceOf(InvalidDataAccessApiUsageException.class)
            .hasRootCauseMessage("추천받은 지하철 정보가 없습니다.");
    }
}
