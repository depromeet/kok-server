package com.kok.kokapi.vote.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.kok.kokapi.common.template.RepositoryTest;
import com.kok.kokcore.vote.domain.Candidate;
import com.kok.kokcore.vote.domain.Vote;
import com.kok.kokcore.vote.domain.vo.VoteStatus;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

class VoteCommandRedisAdapterTest extends RepositoryTest {

    private static final String VOTES_KEY = "vote:";
    private static final String CANDIDATE_KEY = "candidate:";

    @Autowired
    private VoteCommandRedisAdapter voteCommandRedisAdapter;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @DisplayName("사용자의 투표 결과를 저장한다.")
    @Test
    void saveAllVotesOfMember() {
        // given
        String roomId = "roomId";
        long stationId = 1;
        Candidate candidate = new Candidate(roomId, stationId);
        List<Vote> votes = List.of(
            new Vote(candidate, "memberId1", VoteStatus.AGREE),
            new Vote(candidate, "memberId2", VoteStatus.DISAGREE),
            new Vote(candidate, "memberId3", VoteStatus.DISAGREE)
        );

        // when
        voteCommandRedisAdapter.saveAll(votes);

        // then
        Set<Object> agreeResult = redisTemplate.opsForSet()
            .members(getKey(roomId, stationId, VoteStatus.AGREE));
        Set<Object> disagreeResult = redisTemplate.opsForSet()
            .members(getKey(roomId, stationId, VoteStatus.DISAGREE));
        assertAll(
            () -> assertThat(agreeResult).containsExactlyInAnyOrder("memberId1"),
            () -> assertThat(disagreeResult).containsExactlyInAnyOrder("memberId2", "memberId3")
        );
    }

    private String getKey(String roomId, long stationId, VoteStatus voteStatus) {
        StringJoiner joiner = new StringJoiner(":");
        joiner.add(VOTES_KEY + roomId);
        joiner.add(CANDIDATE_KEY + stationId);
        joiner.add(voteStatus.getName());
        return joiner.toString();
    }
}
