package com.kok.kokapi.room.application.service;

import com.kok.kokcore.room.application.port.out.LoadRoomPort;
import com.kok.kokcore.room.domain.Room;
import com.kok.kokcore.room.usecase.GetRoomUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomQueryService implements GetRoomUseCase {

    private final LoadRoomPort loadRoomPort;
    private static final String PARTICIPANT_KEY_PREFIX = "room:participants:";
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public Room findRoomById(String roomId) {
        return loadRoomPort.findRoomById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found with id: " + roomId));
    }

    @Override
    public List<String> getParticipants(String roomId) {
        String key = PARTICIPANT_KEY_PREFIX + roomId;
        return redisTemplate.opsForList().range(key, 0, -1);
    }
}
