package com.kok.kokapi.room.adapter.out.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kok.kokcore.room.application.port.out.SaveRoomParticipantsPort;
import com.kok.kokcore.room.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RoomParticipantSaveAdapter implements SaveRoomParticipantsPort {

    private static final String PARTICIPANT_KEY_PREFIX = "room:participants:";
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void joinRoom(String roomId, Member member) {
        String key = PARTICIPANT_KEY_PREFIX + roomId;
        try {
            String memberJson = objectMapper.writeValueAsString(member);
            redisTemplate.opsForList().rightPush(key, memberJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("failed to serialize member");
        }
    }
}
