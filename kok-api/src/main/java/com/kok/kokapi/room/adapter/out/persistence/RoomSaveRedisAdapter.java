package com.kok.kokapi.room.adapter.out.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kok.kokapi.common.util.RedisExecutor;
import com.kok.kokcore.room.domain.Room;
import com.kok.kokcore.room.port.out.SaveRoomPort;
import com.kok.kokcore.room.port.out.UpdateRoomPort;
import java.time.Duration;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
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
            RedisExecutor.runOrThrow("saveRoom",
                () -> redisTemplate.opsForValue().set(key, roomJson, ROOM_TTL));
        } catch (JsonProcessingException e) {
            log.error("[Room] Failed to serialize room. roomId={}", room.getId(), e);
            throw new RuntimeException("failed to serialize room object", e);
        } catch (Exception e) {
            log.error("[Room] Save to Redis failed. roomId={}, key={}", room.getId(), key, e);
            throw new RuntimeException(
                "failed to save room to Redis: " + e.getClass().getSimpleName(), e);
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
            log.error("[Room] Failed to serialize room. roomId={}", room.getId(), e);
            throw new RuntimeException("failed to serialize room object", e);
        } catch (RuntimeException e) {
            log.error("[Room] update to Redis failed. roomId={}, key={}", room.getId(), key, e);
            throw new RuntimeException(
                "failed to update room in Redis: " + e.getClass().getSimpleName(), e);
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
