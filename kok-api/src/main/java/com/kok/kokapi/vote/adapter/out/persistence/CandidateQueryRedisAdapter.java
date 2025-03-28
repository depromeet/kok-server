package com.kok.kokapi.vote.adapter.out.persistence;

import com.kok.kokapi.common.util.RedisExecutor;
import com.kok.kokcore.vote.domain.Candidate;
import com.kok.kokcore.vote.port.out.LoadCandidatePort;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
@RequiredArgsConstructor
public class CandidateQueryRedisAdapter implements LoadCandidatePort {

    private static final String CANDIDATE_KEY_FORMAT = "vote:%s:candidates";

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public List<Candidate> findByRoomId(String roomId) {
        Set<Object> stationIds = RedisExecutor.runOrElseGet("findByRoomId", () ->
            redisTemplate.opsForSet().members(getCandidateKey(roomId)), Set.of()
        );

        return stationIds.stream()
            .map(this::getStationId)
            .filter(Objects::nonNull)
            .map(stationId -> new Candidate(roomId, stationId))
            .toList();
    }

    private Long getStationId(Object stationId) {
        try {
            return Long.parseLong(stationId.toString());
        } catch (NumberFormatException e) {
            log.warn("Invalid stationId format in Redis: {}", stationId, e);
            return null;
        }
    }

    @Override
    public boolean isExistsByRoomId(String roomId) {
        return RedisExecutor.runOrElseGet("isExistsByRoomId", () ->
            Boolean.TRUE.equals(redisTemplate.hasKey(getCandidateKey(roomId))), false);
    }

    private String getCandidateKey(String roomId) {
        return String.format(CANDIDATE_KEY_FORMAT, roomId);
    }
}
