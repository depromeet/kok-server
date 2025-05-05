package com.kok.kokapi.room.adapter.out.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kok.kokapi.common.util.RedisExecutor;
import com.kok.kokcore.room.domain.Member;
import com.kok.kokcore.room.port.out.SaveRoomParticipantsPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RoomParticipantSaveAdapter implements SaveRoomParticipantsPort {

    private static final String PARTICIPANT_KEY_PREFIX = "room:participants:";
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public int joinRoom(String roomId, Member member) {
        String key = PARTICIPANT_KEY_PREFIX + roomId;

        try {
            String memberJson = objectMapper.writeValueAsString(member);
            RedisExecutor.runOrThrow("joinRoom:push" + roomId,
                () -> redisTemplate.opsForList().rightPush(key, memberJson));
            Long participantCount = RedisExecutor.runOrThrow("joinRoom:size: " + roomId,
                () -> redisTemplate.opsForList().size(key));
            return participantCount != null ? participantCount.intValue() : 0;
        } catch (JsonProcessingException e) {
            log.error("[RoomParticipant] Failed to serialize member. roomId={}, member={}", roomId, member, e);
            throw new RuntimeException("Failed to serialize member", e);
        } catch (Exception e) {
            log.error("[RoomParticipant] Redis join failed. roomId={}, member={}", roomId, member, e);
            throw new RuntimeException("Failed to join room in Redis: " + e.getClass().getSimpleName(), e);
        }
    }
}
