package com.kok.kokapi.vote.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import com.kok.kokapi.common.template.RepositoryTest;
import com.kok.kokcore.vote.domain.Candidate;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

class CandidateCommandRedisAdapterTest extends RepositoryTest {

    private static final String CANDIDATE_KEY_FORMAT = "vote:%s:candidates";

    @Autowired
    private CandidateCommandRedisAdapter candidateCommandRedisAdapter;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @DisplayName("Candidate을 저장한다.")
    @Test
    void saveAllCandidates() {
        // given
        String roomId = "roomId";
        String key = getCandidateKey(roomId);
        List<Candidate> candidates = List.of(
            new Candidate(roomId, 1),
            new Candidate(roomId, 2)
        );

        // when
        candidateCommandRedisAdapter.saveAll(candidates);

        // then
        Set<Object> results = redisTemplate.opsForSet().members(key);
        assertThat(results).containsExactlyInAnyOrder(1, 2);
    }

    private String getCandidateKey(String roomId) {
        return String.format(CANDIDATE_KEY_FORMAT, roomId);
    }
}
