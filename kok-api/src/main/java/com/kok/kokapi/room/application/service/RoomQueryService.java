package com.kok.kokapi.room.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kok.kokcore.room.port.out.LoadRoomParticipantPort;
import com.kok.kokcore.room.port.out.LoadRoomPort;
import com.kok.kokcore.room.domain.Member;
import com.kok.kokcore.room.domain.Room;
import com.kok.kokcore.room.usecase.GetRoomUseCase;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomQueryService implements GetRoomUseCase {

    private static final String PARTICIPANT_KEY_PREFIX = "room:participants:";

    private final LoadRoomPort loadRoomPort;
    private final LoadRoomParticipantPort loadRoomParticipantPort;
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public Room findRoomById(String roomId) {
        return loadRoomPort.findRoomById(roomId)
            .orElseThrow(() -> new IllegalArgumentException("Room not found with id: " + roomId));
    }

    @Override
    public List<Member> getParticipants(String roomId) {
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

    @Override
    public int getParticipantsCount(String roomId) {
        Long participantCount = loadRoomParticipantPort.countParticipantsById(roomId);
        return participantCount.intValue();
    }
}
