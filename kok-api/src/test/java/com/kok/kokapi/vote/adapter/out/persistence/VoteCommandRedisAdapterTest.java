package com.kok.kokapi.vote.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.kok.kokapi.common.template.RepositoryTest;
import com.kok.kokcore.vote.domain.Candidate;
import com.kok.kokcore.vote.domain.Vote;
import com.kok.kokcore.vote.domain.vo.VoteStatus;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

class VoteCommandRedisAdapterTest extends RepositoryTest {

    @Autowired
    private VoteCommandRedisAdapter voteCommandRedisAdapter;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    @DisplayName("멤버별 투표 내용을 Hash로 저장한다.")
    void saveVoteMemberHash() {
        // given
        String roomId = "roomId";
        String memberId = "memberId";
        List<Vote> votes = List.of(
            new Vote(new Candidate(roomId, 1), memberId, VoteStatus.AGREE),
            new Vote(new Candidate(roomId, 2), memberId, VoteStatus.DISAGREE)
        );

        // when
        voteCommandRedisAdapter.saveVoteMemberHash(votes);

        // then
        String key = VoteKey.memberKey(roomId, memberId);
        Map<Object, Object> result = redisTemplate.opsForHash().entries(key);
        assertThat(result).containsExactlyInAnyOrderEntriesOf(Map.of(
            "1", VoteStatus.AGREE.getName(),
            "2", VoteStatus.DISAGREE.getName()));
    }

    @Test
    @DisplayName("투표 완료자 Set에 멤버를 저장한다.")
    void saveVotedMemberSet() {
        // given
        String roomId = "roomId";
        String memberId = "memberId";

        // when
        voteCommandRedisAdapter.saveVotedMemberSet(roomId, memberId);

        // then
        Set<Object> result = redisTemplate.opsForSet().members(VoteKey.voteKey(roomId));
        assertThat(result).containsExactlyInAnyOrder(memberId);
    }

    @Test
    @DisplayName("찬/반 Set에 멤버를 저장한다.")
    void saveVoteStatusSet() {
        // given
        Vote vote = new Vote(new Candidate("roomId", 100), "memberId", VoteStatus.AGREE);

        // when
        voteCommandRedisAdapter.saveVoteStatusSet(vote);

        // then
        Set<Object> result = redisTemplate.opsForSet()
            .members(VoteKey.voteStatusMemberSetKey(vote));
        assertThat(result).containsExactlyInAnyOrder("memberId");
    }

    @Test
    @DisplayName("ZSet에 찬/반 득표수를 1 증가시킨다.")
    void incrementVoteStatusCountZSet() {
        // given
        Vote vote = new Vote(new Candidate("roomId", 100), "memberId", VoteStatus.AGREE);

        // when
        voteCommandRedisAdapter.incrementVoteStatusCountZSet(vote);

        // then
        Double score = redisTemplate.opsForZSet()
            .score(VoteKey.voteStatusCountZSetKey(vote), vote.getStationId());

        assertThat(score).isEqualTo(1.0);
    }

    @Test
    @DisplayName("ZSet에서 득표수를 1 감소시킨다.")
    void decrementVoteCountInZSet() {
        // given
        Vote vote = new Vote(new Candidate("roomId", 100), "memberId", VoteStatus.AGREE);
        String key = VoteKey.voteStatusCountZSetKey(vote);
        redisTemplate.opsForZSet().add(key, vote.getStationId(), 2.0);

        // when
        voteCommandRedisAdapter.decrementVoteCountInZSet(vote);

        // then
        Double score = redisTemplate.opsForZSet().score(key, vote.getStationId());
        assertThat(score).isEqualTo(1.0);
    }

    @Test
    @DisplayName("찬/반 Set에서 멤버를 제거한다.")
    void removeMemberFromVoteStatusSet() {
        // given
        Vote vote = new Vote(new Candidate("roomId", 100), "memberId", VoteStatus.AGREE);
        String key = VoteKey.voteStatusMemberSetKey(vote);
        redisTemplate.opsForSet().add(key, "memberId");

        // when
        voteCommandRedisAdapter.removeMemberFromVoteStatusSet(vote);

        // then
        Set<Object> result = redisTemplate.opsForSet().members(key);
        assertThat(result).doesNotContain("memberId");
    }

    @Test
    @DisplayName("멤버 투표 Hash를 삭제한다.")
    void deleteMemberVoteHash() {
        // given
        String roomId = "roomId";
        String memberId = "memberId";
        String key = VoteKey.memberKey(roomId, memberId);
        redisTemplate.opsForHash().put(key, "1", "AGREE");

        // when
        voteCommandRedisAdapter.deleteMemberVoteHash(roomId, memberId);

        // then
        assertAll(
            () -> assertThat(redisTemplate.hasKey(key)).isFalse(),
            () -> assertThat(redisTemplate.opsForHash().size(key)).isZero()
        );
    }

    @Test
    @DisplayName("투표 완료자 Set에서 멤버를 제거한다.")
    void removeMemberFromVotedSet() {
        // given
        String roomId = "roomId";
        String memberId = "memberId";
        String key = VoteKey.voteKey(roomId);
        redisTemplate.opsForSet().add(key, memberId);

        // when
        voteCommandRedisAdapter.removeMemberFromVotedSet(roomId, memberId);

        // then
        Set<Object> result = redisTemplate.opsForSet().members(key);
        assertThat(result).doesNotContain(memberId);
    }
}
