package com.kok.kokapi.vote.adapter.out.persistence;

import com.kok.kokcore.vote.application.port.out.LoadCandidatePort;
import com.kok.kokcore.vote.domain.Candidate;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CandidateQueryRedisAdapter implements LoadCandidatePort {

    private static final String CANDIDATES_KEY = "vote:candidates:";

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public List<Candidate> findByRoomId(String roomId) {
        Set<Object> stationIds = redisTemplate.opsForSet().members(getKey(roomId));
        if (Objects.isNull(stationIds)) {
            return List.of();
        }
        return stationIds.stream()
            .map(stationId -> new Candidate(roomId, Long.parseLong(stationId.toString())))
            .toList();
    }

    @Override
    public boolean isExistsByRoomId(String roomId) {
        return redisTemplate.hasKey(getKey(roomId));
    }

    private String getKey(String roomId) {
        return CANDIDATES_KEY + roomId;
    }
}
