package com.kok.kokapi.vote.adapter.out.persistence;

import com.kok.kokcore.vote.application.port.out.LoadVotePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class VoteQueryRedisAdapter implements LoadVotePort {

    private static final String MEMBER_VOTE_KEY_FORMAT = "vote:%s:member:%s";

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean isExistsByRoomIdAndMemberId(String roomId, String memberId) {
        String key = getMemberVoteKey(roomId, memberId);
        return !redisTemplate.opsForHash().entries(key).isEmpty();
    }

    private String getMemberVoteKey(String roomId, String memberId) {
        return String.format(MEMBER_VOTE_KEY_FORMAT, roomId, memberId);
    }
}
