package com.kok.kokapi.room.adapter.out.persistence;

import com.kok.kokcore.room.application.port.out.LoadMemberPort;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RoomParticipantQueryRedisAdapter implements LoadMemberPort {

    private static final String PARTICIPANT_KEY_PREFIX = "room:participants:";

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public long countParticipantsByRoomId(String roomId) {
        String key = buildKey(roomId);
        Long participantsCount = redisTemplate.opsForList().size(key);
        if(Objects.isNull(participantsCount)){
            return 0;
        }
        return participantsCount;
    }

    private String buildKey(String roomId) {
        return PARTICIPANT_KEY_PREFIX + roomId;
    }
}
