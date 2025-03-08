package com.kok.kokapi.room.adapter.out.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kok.kokcore.room.application.port.out.LoadRoomParticipantsPort;
import com.kok.kokcore.room.domain.Member;
import com.kok.kokcore.room.domain.Profile;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RoomParticipantQueryAdapter implements LoadRoomParticipantsPort {

    private static final String PARTICIPANT_KEY_PREFIX = "room:participants:";
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public List<Profile> getProfilesByRoomId(String roomId) {
        String key = PARTICIPANT_KEY_PREFIX + roomId;
        List<String> profileJson = redisTemplate.opsForList().range(key, 0, -1);

        if (profileJson == null || profileJson.isEmpty()) {
            return List.of();
        }

        return profileJson.stream()
                .map(this::deserializeProfile)
                .toList();
    }

    private Profile deserializeProfile(String profileJson) {
        try {
            Member member = objectMapper.readValue(profileJson, Member.class);
            return new Profile(member.getProfile(), member.getNickname());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("failed to deserialize profile");
        }
    }
}
