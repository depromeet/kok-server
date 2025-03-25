package com.kok.kokapi.vote.adapter.out.persistence;

import com.kok.kokcore.vote.application.port.out.LoadVotePort;
import java.util.StringJoiner;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class VoteQueryRedisAdapter implements LoadVotePort {

    private static final String VOTES_KEY = "vote:";
    private static final String MEMBER_KEY = "member:";

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean isExistsByRoomIdAndMemberId(String roomId, String memberId) {
        String key = getMemberKey(roomId, memberId);
        return !redisTemplate.opsForHash().entries(key).isEmpty();
    }

    private String getMemberKey(String roomId, String memberId) {
        StringJoiner joiner = new StringJoiner(":");
        joiner.add(VOTES_KEY + roomId);
        joiner.add(MEMBER_KEY + memberId);
        return joiner.toString();
    }
}
