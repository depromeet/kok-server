package com.kok.kokapi.station.adapter.out.persistence;

import com.kok.kokcore.station.application.port.out.LoadRecommendedStationsPort;
import com.kok.kokcore.station.domain.entity.Station;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Repository;

@Repository
public class StationCacheAdapter implements LoadRecommendedStationsPort {

    public static final String RECOMMEND_STATIONS_CACHE_KEY = "recommendStations";

    @Qualifier("stationCacheManager")
    private final CacheManager cacheManager;

    public StationCacheAdapter(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Station> getStationsByRoomId(String roomId) {
        Cache cache = cacheManager.getCache(RECOMMEND_STATIONS_CACHE_KEY);
        validateCache(cache);
        Cache.ValueWrapper wrapper = cache.get(roomId);
        validateWrapper(wrapper);
        return (List<Station>) wrapper.get();
    }

    private void validateCache(Cache cache) {
        if (Objects.isNull(cache)) {
            throw new IllegalStateException(
                "No Cache for \"" + RECOMMEND_STATIONS_CACHE_KEY + "\".");
        }
    }

    private void validateWrapper(ValueWrapper wrapper) {
        if (Objects.isNull(wrapper)) {
            throw new IllegalStateException("추천받은 지하철 정보가 없습니다.");
        }
    }
}
