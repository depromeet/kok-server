package com.kok.kokapi.room.application.service;

import com.kok.kokcore.room.usecase.JoinRoomUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomParticipantService implements JoinRoomUseCase {

    private static final String PARTICIPANT_KEY_PREFIX = "room:participants:";
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void joinRoom(String roomId, String profile) {
        String key = PARTICIPANT_KEY_PREFIX + roomId;
        redisTemplate.opsForList().rightPush(key, profile);
    }
}
