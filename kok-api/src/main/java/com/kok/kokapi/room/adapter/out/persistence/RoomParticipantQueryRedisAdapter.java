package com.kok.kokapi.room.adapter.out.persistence;

import com.kok.kokcore.room.application.port.out.LoadRoomParticipantPort;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RoomParticipantQueryRedisAdapter implements LoadRoomParticipantPort {

    public static final String PARTICIPANT_KEY_PREFIX = "room:participants";
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public Long countParticipantsById(String roomId) {
        String key = buildKey(roomId);
        return Objects.requireNonNull(redisTemplate.opsForList().size(key));
    }

    private String buildKey(String roomId) {
        return PARTICIPANT_KEY_PREFIX + roomId;
    }
}
