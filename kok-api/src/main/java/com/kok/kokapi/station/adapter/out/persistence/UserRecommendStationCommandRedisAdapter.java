package com.kok.kokapi.station.adapter.out.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kok.kokcore.station.domain.entity.Station;
import com.kok.kokcore.station.port.out.SaveUserRecommendStationsPort;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class UserRecommendStationCommandRedisAdapter implements SaveUserRecommendStationsPort {

    private final String USER_RECOMMEND_STATION_PREFIX = "userRecommendStation:";
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public Station addUserRecommendStation(String roomId, Station station) {
        String key = buildKey(roomId);

        try {
            String stationJson = redisTemplate.opsForValue().get(key);
            List<Station> stationList = (stationJson == null)
                ? new ArrayList<>()
                : objectMapper.readValue(stationJson, new TypeReference<>() {
                });

            stationList.add(station);
            String updatedJson = objectMapper.writeValueAsString(stationList);
            redisTemplate.opsForValue().set(key, updatedJson);
            return station;

        } catch (JsonProcessingException e) {
            return null;
        }
    }

    private String buildKey(String roomId) {
        return USER_RECOMMEND_STATION_PREFIX + roomId;
    }
}
