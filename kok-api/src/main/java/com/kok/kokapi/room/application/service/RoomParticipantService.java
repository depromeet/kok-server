package com.kok.kokapi.room.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kok.kokcore.room.domain.Member;
import com.kok.kokcore.room.domain.Profile;
import com.kok.kokcore.room.usecase.GetMemberProfileUseCase;
import com.kok.kokcore.room.usecase.JoinRoomUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class RoomParticipantService implements JoinRoomUseCase, GetMemberProfileUseCase {

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

    @Override
    public List<Profile> getProfilesByRoomId(String roomId) {
        return List.of();
    }
}
