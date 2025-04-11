package com.kok.kokapi.vote.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.kok.kokapi.common.template.RepositoryTest;
import com.kok.kokcore.vote.domain.Candidate;
import com.kok.kokcore.vote.domain.Vote;
import java.util.List;
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
    @DisplayName("멤버별로 투표한 곳을 Set으로 저장한다.")
    void saveVotes() {
        // given
        String roomId = "roomId";
        String memberId = "memberId";
        List<Long> stationIds = List.of(1L, 2L);

        // when
        voteCommandRedisAdapter.saveVotedStationsByRoomIdAndMemberId(stationIds, roomId, memberId);

        // then
        String key = VoteKey.memberKey(roomId, memberId);
        Set<Object> result = redisTemplate.opsForSet().members(key);
        assertThat(result).containsExactlyInAnyOrder(1, 2);
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
    @DisplayName("특정 후보지에 투표한 멤버를 저장한다.")
    void saveVotedMembersByRoomIdAndStationId() {
        // given
        String roomId = "roomId";
        long stationId = 100;
        String memberId = "memberId";
        Vote vote = new Vote(new Candidate(roomId, stationId), memberId);

        // when
        voteCommandRedisAdapter.saveVotedMembersByRoomIdAndStationId(memberId, roomId, stationId);

        // then
        Set<Object> result = redisTemplate.opsForSet()
            .members(VoteKey.votedMembersOfStationKey(vote));
        assertThat(result).containsExactlyInAnyOrder("memberId");
    }

    @Test
    @DisplayName("특정 후보지의 득표수를 1 증가시킨다.")
    void incrementVoteStatusCountZSet() {
        // given
        String roomId = "roomId";
        long stationId = 100;
        Vote vote = new Vote(new Candidate(roomId, stationId), "memberId");

        // when
        voteCommandRedisAdapter.increaseVotedCountByRoomIdAndStationId(roomId, stationId);

        // then
        Double score = redisTemplate.opsForZSet()
            .score(VoteKey.votedCountOfStationIdKey(vote), vote.getStationId());

        assertThat(score).isEqualTo(1.0);
    }

    @Test
    @DisplayName("ZSet에서 득표수를 1 감소시킨다.")
    void decrementVoteCountInZSet() {
        // given
        Vote vote = new Vote(new Candidate("roomId", 100), "memberId");
        String key = VoteKey.votedCountOfStationIdKey(vote);
        redisTemplate.opsForZSet().add(key, vote.getStationId(), 2.0);

        // when
        voteCommandRedisAdapter.decrementVoteCountInZSet(vote);

        // then
        Double score = redisTemplate.opsForZSet().score(key, vote.getStationId());
        assertThat(score).isEqualTo(1.0);
    }

    @Test
    @DisplayName("후보지에 투표한 멤버 Set에서 멤버를 제거한다.")
    void removeMemberFromVoteStatusSet() {
        // given
        Vote vote = new Vote(new Candidate("roomId", 100), "memberId");
        String key = VoteKey.votedMembersOfStationKey(vote);
        redisTemplate.opsForSet().add(key, "memberId");

        // when
        voteCommandRedisAdapter.removeMemberFromVoteStatusSet(vote);

        // then
        Set<Object> result = redisTemplate.opsForSet().members(key);
        assertThat(result).doesNotContain("memberId");
    }

    @Test
    @DisplayName("멤버 투표 Hash를 삭제한다.")
    void deleteVotesByRoomIdAndMemberId() {
        // given
        String roomId = "roomId";
        String memberId = "memberId";
        String key = VoteKey.memberKey(roomId, memberId);
        redisTemplate.opsForHash().put(key, "1", "AGREE");

        // when
        voteCommandRedisAdapter.deleteVotesByRoomIdAndMemberId(roomId, memberId);

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
