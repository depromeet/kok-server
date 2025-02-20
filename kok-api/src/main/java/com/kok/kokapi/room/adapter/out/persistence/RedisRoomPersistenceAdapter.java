package com.kok.kokapi.room.adapter.out.persistence;

import com.kok.kokcore.application.port.out.RoomRepository;
import com.kok.kokcore.domain.Room;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
public class RedisRoomPersistenceAdapter implements RoomRepository {

    private static final String ROOM_KEY_PREFIX = "room";
    private static final Duration ROOM_TTL = Duration.ofDays(3);
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisRoomPersistenceAdapter(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Room save(Room room) {
        String key = buildKey(room.getId());
        redisTemplate.opsForValue().set(key, room, ROOM_TTL);
        return room;
    }

    @Override
    public Room findById(String id) {
        String key = buildKey(id);
        return (Room) redisTemplate.opsForValue().get(key);
    }

    private String buildKey(String roomId) {
        return ROOM_KEY_PREFIX + ":" + roomId;
    }
}
