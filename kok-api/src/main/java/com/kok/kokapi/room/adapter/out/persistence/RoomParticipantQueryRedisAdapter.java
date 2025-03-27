package com.kok.kokapi.room.adapter.out.persistence;

import com.kok.kokcore.room.port.out.LoadRoomParticipantPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RoomParticipantQueryRedisAdapter implements LoadRoomParticipantPort {

    public static final String PARTICIPANT_KEY_PREFIX = "room:participants:";

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public Long countParticipantsById(String roomId) {
        String key = buildKey(roomId);
        if (!redisTemplate.hasKey(key)) {
            return 0L;
        }
        return redisTemplate.opsForList().size(key);
    }

    private String buildKey(String roomId) {
        return PARTICIPANT_KEY_PREFIX + roomId;
    }
}
