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

    private static final String CANDIDATE_KEY_FORMAT = "vote:%s:candidates";

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public List<Candidate> findByRoomId(String roomId) {
        Set<Object> stationIds = redisTemplate.opsForSet().members(getCandidateKey(roomId));
        if (Objects.isNull(stationIds)) {
            return List.of();
        }
        return stationIds.stream()
            .map(stationId -> new Candidate(roomId, Long.parseLong(stationId.toString())))
            .toList();
    }

    @Override
    public boolean isExistsByRoomId(String roomId) {
        return redisTemplate.hasKey(getCandidateKey(roomId));
    }

    private String getCandidateKey(String roomId) {
        return String.format(CANDIDATE_KEY_FORMAT, roomId);
    }
}
