package com.kok.kokapi.station.adapter.out.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kok.kokcore.station.domain.entity.Station;
import com.kok.kokcore.station.port.out.SaveCustomStationsPort;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class CustomStationCommandRedisAdapter implements SaveCustomStationsPort {

    private final String CUSTOM_STATION_PREFIX = "customStations:";
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public Station addCustomStations(String roomId, Station station) {
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
        return CUSTOM_STATION_PREFIX + roomId;
    }
}
