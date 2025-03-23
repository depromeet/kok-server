package com.kok.kokapi.vote.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import com.kok.kokapi.common.template.RepositoryTest;
import com.kok.kokcore.vote.domain.Candidate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

class CandidateQueryRedisAdapterTest extends RepositoryTest {

    private static final String CANDIDATES_KEY = "vote:candidates:";

    @Autowired
    private CandidateQueryRedisAdapter candidateQueryRedisAdapter;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @DisplayName("roomId로 후보지를 조회한다.")
    @Test
    void findByRoomId() {
        // given
        String roomId = "roomId";
        String key = CANDIDATES_KEY + roomId;
        Candidate candidate = new Candidate(roomId, 1);
        Candidate candidate2 = new Candidate(roomId, 2);
        Candidate candidate3 = new Candidate(roomId, 3);
        redisTemplate.opsForSet().add(key, "1", "2", "3");

        // when
        List<Candidate> result = candidateQueryRedisAdapter.findByRoomId(roomId);

        // then
        assertThat(result).containsExactlyInAnyOrder(candidate, candidate2, candidate3);
    }

    @DisplayName("roomId에 해당하는 후보지가 없으면 빈 리스트를 반환한다.")
    @Test
    void findByRoomIdWhenNotExist() {
        // given
        String roomId = "nonexistent";

        // when
        List<Candidate> result = candidateQueryRedisAdapter.findByRoomId(roomId);

        // then
        assertThat(result).isEmpty();
    }

    @DisplayName("roomId에 대한 후보지가 존재한다.")
    @Test
    void isExistsByRoomId() {
        // given
        String roomId = "roomId";
        String key = CANDIDATES_KEY + roomId;
        redisTemplate.opsForSet().add(key, "1");

        // when
        boolean result = candidateQueryRedisAdapter.isExistsByRoomId(roomId);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("roomId에 대한 후보지가 존재하지 않는다.")
    @Test
    void doesNotExistsByRoomId() {
        // given
        String roomId = "unknownRoomId";

        // when
        boolean result = candidateQueryRedisAdapter.isExistsByRoomId(roomId);

        // then
        assertThat(result).isFalse();
    }
}
