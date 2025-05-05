package com.kok.kokapi.vote.adapter.out.persistence;

import com.kok.kokapi.common.util.RedisExecutor;
import com.kok.kokcore.vote.domain.Candidate;
import com.kok.kokcore.vote.port.out.SaveCandidatePort;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CandidateCommandRedisAdapter implements SaveCandidatePort {

    private static final String CANDIDATE_KEY_FORMAT = "candidate:%s";

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void saveAll(List<Candidate> candidates) {
        validate(candidates);
        String key = getCandidateKey(candidates);
        Object[] stationIds = candidates.stream()
            .map(Candidate::getStationId)
            .toArray();

        RedisExecutor.runOrThrow("saveAll", () ->
            redisTemplate.opsForSet().add(key, stationIds)
        );
    }

    private void validate(List<Candidate> candidates) {
        if (Objects.isNull(candidates) || candidates.isEmpty()) {
            throw new IllegalArgumentException("No candidates to save");
        }
    }

    private String getCandidateKey(List<Candidate> candidates) {
        String roomId = candidates.getFirst().getRoomId();
        return String.format(CANDIDATE_KEY_FORMAT, roomId);
    }
}
