package com.kok.kokapi.station.adapter.out.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kok.kokcore.station.domain.entity.Station;
import com.kok.kokcore.station.port.out.ReadUserRecommendStationsPort;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRecommendStationQueryRedisAdapter implements ReadUserRecommendStationsPort {

    private static final String USER_RECOMMEND_STATION_PREFIX = "userRecommendStations:";

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public List<Station> findUserRecommendedStationsByRoomId(String roomId) {
        String key = buildKey(roomId);
        String stationJson = redisTemplate.opsForValue().get(key);

        if (stationJson == null) {
            return Collections.emptyList();
        }

        try {
            return objectMapper.readValue(stationJson, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            return Collections.emptyList();
        }
    }

    private String buildKey(String roomId) {
        return USER_RECOMMEND_STATION_PREFIX + roomId;
    }
}
