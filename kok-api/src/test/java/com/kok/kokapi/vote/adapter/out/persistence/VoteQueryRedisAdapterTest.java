package com.kok.kokapi.vote.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import com.kok.kokapi.common.template.RepositoryTest;
import com.kok.kokcore.vote.domain.vo.VoteStatus;
import java.util.Map;
import java.util.StringJoiner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

class VoteQueryRedisAdapterTest extends RepositoryTest {

    private static final String VOTES_KEY = "vote:";
    private static final String MEMBER_KEY = "member:";

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
        String key = getMemberKey(roomId, memberId);
        redisTemplate.opsForHash().putAll(key, Map.of(1L, VoteStatus.AGREE.isAgree()));

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

    private String getMemberKey(String roomId, String memberId) {
        StringJoiner joiner = new StringJoiner(":");
        joiner.add(VOTES_KEY + roomId);
        joiner.add(MEMBER_KEY + memberId);
        return joiner.toString();
    }
}
