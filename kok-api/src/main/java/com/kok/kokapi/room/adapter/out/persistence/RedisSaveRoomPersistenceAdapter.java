package com.kok.kokapi.room.adapter.out.persistence;

import com.kok.kokcore.application.port.out.SaveRoomPort;
import com.kok.kokcore.domain.Room;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
public class RedisSaveRoomPersistenceAdapter implements SaveRoomPort {

    private static final String ROOM_KEY_PREFIX = "room";
    private static final Duration ROOM_TTL = Duration.ofDays(3);
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisSaveRoomPersistenceAdapter(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Room save(Room room) {
        String key = buildKey(room.getId());
        redisTemplate.opsForValue().set(key, room, ROOM_TTL);
        return room;
    }

    private String buildKey(String roomId) {
        return ROOM_KEY_PREFIX + ":" + roomId;
    }
}
