package com.kok.kokapi.room.adapter.out.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kok.kokcore.room.domain.Member;
import com.kok.kokcore.room.port.out.LoadRoomParticipantPort;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RoomParticipantQueryRedisAdapter implements LoadRoomParticipantPort {

    public static final String PARTICIPANT_KEY_PREFIX = "room:participants:";

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public Long countParticipantsById(String roomId) {
        String key = buildKey(roomId);
        if (!redisTemplate.hasKey(key)) {
            return 0L;
        }
        return redisTemplate.opsForList().size(key);
    }

    @Override
    public List<Member> findMembersByRoomId(String roomId) {
        String key = PARTICIPANT_KEY_PREFIX + roomId;
        List<String> memberJson = redisTemplate.opsForList().range(key, 0, -1);
        List<Member> members = new ArrayList<>();
        if (memberJson != null) {
            for (String data : memberJson) {
                try {
                    Member member = objectMapper.readValue(data, Member.class);
                    members.add(member);
                } catch (JsonProcessingException ignored) {
                }
            }
        }
        return members;
    }

    private String buildKey(String roomId) {
        return PARTICIPANT_KEY_PREFIX + roomId;
    }
}
