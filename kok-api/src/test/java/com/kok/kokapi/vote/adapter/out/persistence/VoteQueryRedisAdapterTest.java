package com.kok.kokapi.vote.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import com.kok.kokapi.common.template.RepositoryTest;
import com.kok.kokcore.vote.domain.Vote;
import com.kok.kokcore.vote.domain.vo.VoteStatus;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

class VoteQueryRedisAdapterTest extends RepositoryTest {

    private static final String MEMBER_VOTE_KEY_FORMAT = "vote:%s:member:%s";

    @Autowired
    private VoteQueryRedisAdapter voteQueryRedisAdapter;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @DisplayName("roomId와 memberId 조합으로 투표 정보가 존재하는지 확인한다.")
    @Test
    void isExistsByRoomIdAndMemberId() {
        // given
        String roomId = "roomId";
        String memberId = "memberId";
        String key = getMemberVoteKey(roomId, memberId);
        redisTemplate.opsForHash().putAll(key, Map.of(1L, VoteStatus.AGREE.getName()));

        // when
        boolean result = voteQueryRedisAdapter.isExistsByRoomIdAndMemberId(roomId, memberId);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("해당 roomId와 memberId 조합으로 투표 정보가 없으면 false를 반환한다.")
    @Test
    void isNotExistsByRoomIdAndMemberId() {
        // given
        String roomId = "roomId";
        String memberId = "memberId";

        // when
        boolean result = voteQueryRedisAdapter.isExistsByRoomIdAndMemberId(roomId, memberId);

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("roomId와 memberId 조합으로 모든 투표 정보를 조회한다.")
    @Test
    void findAllByRoomIdAndMemberId() {
        // given
        String roomId = "roomId";
        String memberId = "memberId";
        String key = getMemberVoteKey(roomId, memberId);
        Vote vote = new Vote(roomId, 1L, memberId, VoteStatus.AGREE.getName());
        redisTemplate.opsForHash()
            .putAll(key, Map.of(vote.getStationId(), vote.getVoteStatus().getName()));

        // when
        List<Vote> votes = voteQueryRedisAdapter.findAllByRoomIdAndMemberId(roomId, memberId);

        // then
        assertThat(votes).containsExactlyInAnyOrder(vote);
    }

    @DisplayName("roomId로 투표한 member 수를 반환한다.")
    @Test
    void countMembersByRoomId() {
        // given
        String roomId = "roomId";
        redisTemplate.opsForHash()
            .putAll(getMemberVoteKey(roomId, "1"), Map.of(1L, VoteStatus.AGREE.getName()));
        redisTemplate.opsForHash()
            .putAll(getMemberVoteKey(roomId, "2"), Map.of(1L, VoteStatus.AGREE.getName()));
        redisTemplate.opsForHash()
            .putAll(getMemberVoteKey(roomId, "3"), Map.of(1L, VoteStatus.AGREE.getName()));

        // when
        int count = voteQueryRedisAdapter.countMembersByRoomId(roomId);

        // then
        assertThat(count).isEqualTo(3);
    }

    private String getMemberVoteKey(String roomId, String memberId) {
        return String.format(MEMBER_VOTE_KEY_FORMAT, roomId, memberId);
    }

}
