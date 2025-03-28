package com.kok.kokapi.vote.adapter.out.persistence;

import com.kok.kokcore.vote.domain.Vote;
import com.kok.kokcore.vote.port.out.LoadVotePort;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
@RequiredArgsConstructor
public class VoteQueryRedisAdapter implements LoadVotePort {

    private static final String MEMBER_VOTE_KEY_FORMAT = "vote:%s:member:%s";

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean isExistsByRoomIdAndMemberId(String roomId, String memberId) {
        String key = getMemberVoteKey(roomId, memberId);
        return !redisTemplate.opsForHash().entries(key).isEmpty();
    }

    @Override
    public List<Vote> findAllByRoomIdAndMemberId(String roomId, String memberId) {
        String key = getMemberVoteKey(roomId, memberId);
        List<Vote> votes = new ArrayList<>();
        Map<Object, Object> voteInfos = redisTemplate.opsForHash().entries(key);
        for (Entry<Object, Object> voteInfo : voteInfos.entrySet()) {
            Long stationId = getStationId(voteInfo);
            String voteStatus = voteInfo.getValue().toString();
            votes.add(new Vote(roomId, stationId, memberId, voteStatus));
        }
        return votes;
    }

    private Long getStationId(Entry<Object, Object> voteInfo) {
        try {
            return Long.valueOf(voteInfo.getKey().toString());
        } catch (NumberFormatException e) {
            log.warn("Invalid stationId format in Redis: {}", voteInfo.getKey(), e);
            throw new RuntimeException(
                "Unexpected error while parsing stationId: " + voteInfo.getKey().toString());
        }
    }

    private String getMemberVoteKey(String roomId, String memberId) {
        return String.format(MEMBER_VOTE_KEY_FORMAT, roomId, memberId);
    }
}
