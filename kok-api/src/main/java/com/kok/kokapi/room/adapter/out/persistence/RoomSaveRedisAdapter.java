package com.kok.kokapi.room.adapter.out.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kok.kokcore.room.domain.Room;
import com.kok.kokcore.room.port.out.SaveRoomPort;
import com.kok.kokcore.room.port.out.UpdateRoomPort;
import java.time.Duration;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
@RequiredArgsConstructor
public class RoomSaveRedisAdapter implements SaveRoomPort, UpdateRoomPort {

    private static final String ROOM_KEY_PREFIX = "room";
    private static final Duration ROOM_TTL = Duration.ofDays(3);

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public Room save(Room room) {
        String key = buildKey(room.getId());
        try {
            String roomJson = objectMapper.writeValueAsString(room);
            redisTemplate.opsForValue().set(key, roomJson, ROOM_TTL);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("failed to save room to Redis", e);
        }
        return room;
    }

    @Override
    public void update(Room room) {
        String key = buildKey(room.getId());
        try {
            String roomJson = objectMapper.writeValueAsString(room);
            Duration currentTtl = getTTL(key);
            redisTemplate.opsForValue().set(key, roomJson, currentTtl);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("failed to update room in Redis", e);
        }
    }

    private String buildKey(String roomId) {
        return ROOM_KEY_PREFIX + ":" + roomId;
    }

    private Duration getTTL(String key) {
        Long expireSeconds = redisTemplate.getExpire(key);
        if (Objects.isNull(expireSeconds) || expireSeconds <= 0) {
            log.warn("Cannot find key: {}, initiate expire TTL", key);
            return ROOM_TTL;
        }
        return Duration.ofSeconds(expireSeconds);
    }
}
