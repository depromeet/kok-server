package com.kok.kokapi.vote.adapter.out.persistence;

import com.kok.kokcore.vote.application.port.out.SaveCandidatePort;
import com.kok.kokcore.vote.domain.Candidate;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CandidateCommandRedisAdapter implements SaveCandidatePort {

    private static final String CANDIDATES_KEY = "vote:candidates:";

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void saveAll(List<Candidate> candidates) {
        validate(candidates);
        String key = getKey(candidates);
        Object[] stationIds = candidates.stream()
            .map(Candidate::getStationId)
            .toArray();

        redisTemplate.opsForSet().add(key, stationIds);
    }

    private void validate(List<Candidate> candidates) {
        if (Objects.isNull(candidates) || candidates.isEmpty()) {
            throw new IllegalArgumentException("No candidates to save");
        }
    }

    private String getKey(List<Candidate> candidates) {
        return CANDIDATES_KEY + candidates.getFirst().getRoomId();
    }
}
