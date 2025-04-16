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
        String key = VoteKey.votedStationsByMemberKey(roomId, memberId);
        Set<Object> result = redisTemplate.opsForSet().members(key);
        assertThat(result).containsExactlyInAnyOrder(1, 2);
    }

    @Test
    @DisplayName("투표 완료자 Set에 멤버를 저장한다.")
    void saveVotedMemberByRoomId() {
        // given
        String roomId = "roomId";
        String memberId = "memberId";

        // when
        voteCommandRedisAdapter.saveVotedMemberByRoomId(roomId, memberId);

        // then
        Set<Object> result = redisTemplate.opsForSet()
            .members(VoteKey.voteCompletedMembersKey(roomId));
        assertThat(result).containsExactlyInAnyOrder(memberId);
    }

    @Test
    @DisplayName("특정 후보지에 투표한 멤버를 저장한다.")
    void saveVotedMemberByRoomIdByRoomIdAndStationId() {
        // given
        String roomId = "roomId";
        long stationId = 100;
        String memberId = "memberId";
        Vote vote = new Vote(new Candidate(roomId, stationId), memberId);

        // when
        voteCommandRedisAdapter.saveVotedMemberByRoomIdAndStationId(memberId, roomId, stationId);

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
            .score(VoteKey.votedCountOfStationKey(roomId), vote.getStationId());

        assertThat(score).isEqualTo(1.0);
    }

    @Test
    @DisplayName("ZSet에서 득표수를 1 감소시킨다.")
    void decreaseVotedCountByRoomIdAndStationId() {
        // given
        String roomId = "roomId";
        long stationId = 100;
        Vote vote = new Vote(new Candidate(roomId, stationId), "memberId");
        String key = VoteKey.votedCountOfStationKey(roomId);
        redisTemplate.opsForZSet().add(key, vote.getStationId(), 2.0);

        // when
        voteCommandRedisAdapter.decreaseVotedCountByRoomIdAndStationId(roomId, stationId);

        // then
        Double score = redisTemplate.opsForZSet().score(key, vote.getStationId());
        assertThat(score).isEqualTo(1.0);
    }

    @Test
    @DisplayName("후보지에 투표한 멤버 Set에서 멤버를 제거한다.")
    void deleteVotedMemberByRoomIdAndStationId() {
        // given
        String roomId = "roomId";
        String memberId = "memberId";
        long stationId = 100;
        Vote vote = new Vote(new Candidate(roomId, stationId), memberId);
        String key = VoteKey.votedMembersOfStationKey(vote);
        redisTemplate.opsForSet().add(key, memberId);

        // when
        voteCommandRedisAdapter.deleteVotedMemberByRoomIdAndStationId(memberId, roomId, stationId);

        // then
        Set<Object> result = redisTemplate.opsForSet().members(key);
        assertThat(result).doesNotContain("memberId");
    }

    @Test
    @DisplayName("멤버가 투표한 후보지 Set을 삭제한다.")
    void deleteVotedStationsByRoomIdAndMemberId() {
        // given
        String roomId = "roomId";
        String memberId = "memberId";
        String key = VoteKey.votedStationsByMemberKey(roomId, memberId);
        redisTemplate.opsForHash().put(key, "1", "AGREE");

        // when
        voteCommandRedisAdapter.deleteVotedStationsByRoomIdAndMemberId(roomId, memberId);

        // then
        assertAll(
            () -> assertThat(redisTemplate.hasKey(key)).isFalse(),
            () -> assertThat(redisTemplate.opsForHash().size(key)).isZero()
        );
    }

    @Test
    @DisplayName("투표 완료자 Set에서 멤버를 제거한다.")
    void deleteVotedMemberByRoomId() {
        // given
        String roomId = "roomId";
        String memberId = "memberId";
        String key = VoteKey.voteCompletedMembersKey(roomId);
        redisTemplate.opsForSet().add(key, memberId);

        // when
        voteCommandRedisAdapter.deleteVotedMemberByRoomId(roomId, memberId);

        // then
        Set<Object> result = redisTemplate.opsForSet().members(key);
        assertThat(result).doesNotContain(memberId);
    }

    @Test
    @DisplayName("주어진 stationId에 대해 점수를 station priority으로 Zset을 초기화한다.")
    void initiateVoteCountByRoomIdAndStationIds() {
        // given
        String roomId = "roomId";
        long stationId = 100;
        long priority = 10;
        String key = VoteKey.votedCountOfStationKey(roomId);

        // when
        voteCommandRedisAdapter.initiateVoteScoreByRoomIdAndStationIdsAndStationPriority(roomId,
            stationId, priority);

        // then
        assertThat(redisTemplate.opsForZSet().score(key, 1L)).isEqualTo(10.0);
    }

    @Test
    @DisplayName("이미 득표 수가 존재하는 경우 ZSet 초기화 시 값을 덮어쓰지 않는다.")
    void doesNotInitiateVoteCountDoesNotOverrideExistingScoreIfPresent() {
        // given
        String roomId = "roomId";
        long stationId = 100;
        long priority = 10;
        String key = VoteKey.votedCountOfStationKey(roomId);
        redisTemplate.opsForZSet().add(key, stationId, 5.0);

        // when
        voteCommandRedisAdapter.initiateVoteScoreByRoomIdAndStationIdsAndStationPriority(roomId,
            stationId, priority);

        // then
        Double score = redisTemplate.opsForZSet().score(key, stationId);
        assertThat(score).isEqualTo(5.0);
    }
}
