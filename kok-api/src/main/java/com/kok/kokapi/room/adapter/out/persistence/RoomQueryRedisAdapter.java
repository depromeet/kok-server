package com.kok.kokapi.room.adapter.out.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kok.kokcore.room.application.port.out.LoadRoomPort;
import com.kok.kokcore.room.domain.Room;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RoomQueryRedisAdapter implements LoadRoomPort {

    public static final String ROOM_KEY_PREFIX = "room:";
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public Optional<Room> findRoomById(String roomId) {
        String key = buildKey(roomId);
        String roomJson = redisTemplate.opsForValue().get(key);

        if (roomJson == null) {
            return Optional.empty();
        }

        try {
            Room room = objectMapper.readValue(roomJson, Room.class);
            return Optional.of(room);
        } catch (JsonProcessingException e) {
            return Optional.empty();
        }
    }

    private String buildKey(String roomId) {
        return ROOM_KEY_PREFIX + roomId;
    }
}
