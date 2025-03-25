package com.kok.kokapi.vote.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import com.kok.kokapi.common.template.RepositoryTest;
import com.kok.kokcore.vote.domain.Candidate;
import com.kok.kokcore.vote.domain.Vote;
import com.kok.kokcore.vote.domain.vo.VoteStatus;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

class VoteCommandRedisAdapterTest extends RepositoryTest {

    private static final String VOTES_KEY = "vote:";
    private static final String CANDIDATE_KEY = "candidate:";
    private static final String MEMBER_KEY = "member:";

    @Autowired
    private VoteCommandRedisAdapter voteCommandRedisAdapter;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @DisplayName("사용자의 투표 결과를 저장한다.")
    @Test
    void saveAllByCandidateVotesOfMember() {
        // given
        String roomId = "roomId";
        String memberId = "memberId";
        Candidate candidate = new Candidate(roomId, 1);
        Candidate candidate2 = new Candidate(roomId, 2);
        Candidate candidate3 = new Candidate(roomId, 3);
        List<Vote> votes = List.of(
            new Vote(candidate, memberId, VoteStatus.AGREE),
            new Vote(candidate2, memberId, VoteStatus.DISAGREE),
            new Vote(candidate3, memberId, VoteStatus.DISAGREE)
        );

        // when
        voteCommandRedisAdapter.saveAllByMember(votes);

        // then
        Map<Object, Object> result = redisTemplate.opsForHash()
            .entries(getMemberKey(roomId, memberId));
        assertThat(result).containsAllEntriesOf(
            Map.of(
                1L, votes.get(0).getVoteStatus().getName(),
                2L, votes.get(1).getVoteStatus().getName(),
                3L, votes.get(2).getVoteStatus().getName()
            )
        );
    }

    private String getCandidateKey(String roomId, long stationId, VoteStatus voteStatus) {
        StringJoiner joiner = new StringJoiner(":");
        joiner.add(VOTES_KEY + roomId);
        joiner.add(CANDIDATE_KEY + stationId);
        joiner.add(voteStatus.getName());
        return joiner.toString();
    }

    private String getMemberKey(String roomId, String memberId) {
        StringJoiner joiner = new StringJoiner(":");
        joiner.add(VOTES_KEY + roomId);
        joiner.add(MEMBER_KEY + memberId);
        return joiner.toString();
    }
}
