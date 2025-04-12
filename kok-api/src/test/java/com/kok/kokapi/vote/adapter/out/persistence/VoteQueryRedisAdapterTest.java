package com.kok.kokapi.vote.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import com.kok.kokapi.common.template.RepositoryTest;
import com.kok.kokcore.vote.domain.Vote;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

class VoteQueryRedisAdapterTest extends RepositoryTest {

    @Autowired
    private VoteQueryRedisAdapter voteQueryRedisAdapter;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    @DisplayName("roomId와 memberId 조합으로 투표 정보가 존재하는지 확인한다.")
    void isExistsByRoomIdAndMemberId() {
        // given
        String roomId = "room";
        String memberId = "member";
        String key = VoteKey.votedStationsByMemberKey(roomId, memberId);
        redisTemplate.opsForSet().add(key, 1);

        // when
        boolean result = voteQueryRedisAdapter.isExistsByRoomIdAndMemberId(roomId, memberId);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("투표 정보가 없으면 false를 반환한다.")
    void isNotExistsByRoomIdAndMemberId() {
        assertThat(voteQueryRedisAdapter.isExistsByRoomIdAndMemberId("roomX", "memberY")).isFalse();
    }

    @Test
    @DisplayName("roomId, memberId로 투표 전체 목록을 조회한다.")
    void findAllByRoomIdAndMemberId() {
        // given
        String roomId = "room2";
        String memberId = "memberB";
        String key = VoteKey.votedStationsByMemberKey(roomId, memberId);
        Vote vote = new Vote(roomId, 1L, memberId);
        Vote vote2 = new Vote(roomId, 2L, memberId);
        redisTemplate.opsForSet().add(key, 1, 2);

        // when
        List<Vote> result = voteQueryRedisAdapter.findAllByRoomIdAndMemberId(roomId, memberId);

        // then
        assertThat(result).hasSize(2)
            .containsExactlyInAnyOrder(vote, vote2);
    }

    @Test
    @DisplayName("roomId에 대해 투표 완료한 멤버 수를 반환한다.")
    void countMembersByRoomId() {
        // given
        String roomId = "room3";
        redisTemplate.opsForSet()
            .add(VoteKey.voteCompletedMembersKey(roomId), "member1", "member2", "member3");

        // when
        int count = voteQueryRedisAdapter.countMembersByRoomId(roomId);

        // then
        assertThat(count).isEqualTo(3);
    }

    @Test
    @DisplayName("특정 stationId에 대해 투표한 memberId 리스트를 조회한다.")
    void findMemberIdsByRoomIdAndStationId() {
        // given
        String roomId = "room4";
        long stationId = 11L;
        String key = VoteKey.votedMembersOfStationKey(roomId, stationId);
        redisTemplate.opsForSet().add(key, "member1", "member2");

        // when
        List<String> result = voteQueryRedisAdapter.findMemberIdsByRoomIdAndStationId(roomId,
            stationId);

        // then
        assertThat(result).containsExactlyInAnyOrder("member1", "member2");
    }

    @Test
    @DisplayName("찬성 수가 가장 많은 stationId를 반환한다.")
    void findFirstStationIdByRoomIdOrderByVotedCount() {
        // given
        String roomId = "room5";
        String key = VoteKey.votedCountOfStationKey(roomId);
        redisTemplate.opsForZSet().add(key, "10", 5.0);
        redisTemplate.opsForZSet().add(key, "11", 8.0);

        // when
        long result = voteQueryRedisAdapter.findFirstStationIdByRoomIdOrderByVotedCount(roomId);

        // then
        assertThat(result).isEqualTo(11);
    }

    @Test
    @DisplayName("찬성 투표자가 아무도 없으면 -1을 반환한다.")
    void getFirstStationIdWhenNoVotes() {
        // given
        String roomId = "room6";
        String key = VoteKey.votedCountOfStationKey(roomId);
        redisTemplate.delete(key);

        // when
        long result = voteQueryRedisAdapter.findFirstStationIdByRoomIdOrderByVotedCount(roomId);

        // then
        assertThat(result).isEqualTo(-1L);
    }

    @Test
    @DisplayName("roomId에 대한 stationId들을 찬성 수 내림차순으로 조회한다.")
    void findStationIdsByRoomIdOrderByVotedCount() {
        // given
        String roomId = "room7";
        String key = VoteKey.votedCountOfStationKey(roomId);
        redisTemplate.opsForZSet().add(key, "10", 5.0);
        redisTemplate.opsForZSet().add(key, "11", 8.0);
        redisTemplate.opsForZSet().add(key, "12", 2.0);

        // when
        List<Long> result = voteQueryRedisAdapter.findStationIdsByRoomIdOrderByVotedCount(roomId);

        // then
        assertThat(result).containsExactly(11L, 10L, 12L);
    }
}
