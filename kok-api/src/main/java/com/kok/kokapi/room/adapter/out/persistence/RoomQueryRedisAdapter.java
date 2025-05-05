package com.kok.kokapi.room.adapter.out.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kok.kokcore.room.domain.Room;
import com.kok.kokcore.room.port.out.LoadRoomPort;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RoomQueryRedisAdapter implements LoadRoomPort {

    public static final String ROOM_KEY_PREFIX = "room:";
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public Optional<Room> findRoomById(String roomId) {
        String key = buildKey(roomId);
        return Optional.ofNullable(redisTemplate.opsForValue().get(key))
            .flatMap(this::deserializeRoom);
    }

    @Override
    public boolean isExistsByRoomId(String roomId) {
        String key = buildKey(roomId);
        return redisTemplate.hasKey(key);
    }

    private Optional<Room> deserializeRoom(String roomJson) {
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
