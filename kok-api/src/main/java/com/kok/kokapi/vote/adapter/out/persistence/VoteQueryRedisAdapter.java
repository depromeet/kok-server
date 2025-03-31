package com.kok.kokapi.vote.adapter.out.persistence;

import com.kok.kokapi.common.util.RedisExecutor;
import com.kok.kokcore.vote.domain.Vote;
import com.kok.kokcore.vote.domain.vo.VoteStatus;
import com.kok.kokcore.vote.port.out.LoadVotePort;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class VoteQueryRedisAdapter implements LoadVotePort {

    private static final String MEMBER_VOTE_KEY_FORMAT = "vote:%s:member:%s";
    private static final String CANDIDATE_VOTE_KEY_FORMAT = "vote:%s:candidate:%d:%s";

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean isExistsByRoomIdAndMemberId(String roomId, String memberId) {
        String key = getMemberVoteKey(roomId, memberId);
        return RedisExecutor.runOrElseGet("isExistsByRoomIdAndMemberId", () ->
            !redisTemplate.opsForHash().entries(key).isEmpty(), false
        );
    }

    @Override
    public List<Vote> findAllByRoomIdAndMemberId(String roomId, String memberId) {
        String key = getMemberVoteKey(roomId, memberId);
        return RedisExecutor.runOrElseGet("findAllByRoomIdAndMemberId", () -> {
            Map<Object, Object> voteInfos = redisTemplate.opsForHash().entries(key);
            List<Vote> votes = new ArrayList<>(voteInfos.size());
            for (Entry<Object, Object> voteInfo : voteInfos.entrySet()) {
                Long stationId = getStationId(voteInfo);
                if (stationId == null) {
                    continue;
                }
                String voteStatus = String.valueOf(voteInfo.getValue());
                votes.add(new Vote(roomId, stationId, memberId, voteStatus));
            }
            return votes;
        }, List.of());
    }

    @Override
    public long getFirstStationIdByRoomIdAndVoteStatus(String roomId, VoteStatus voteStatus) {
        return 0;
    }

    private Long getStationId(Entry<Object, Object> voteInfo) {
        try {
            return Long.valueOf(voteInfo.getKey().toString());
        } catch (NumberFormatException e) {
            log.warn("Invalid stationId format in Redis: {}", voteInfo.getKey(), e);
            return null;
        }
    }

    private String getMemberVoteKey(String roomId, String memberId) {
        return String.format(MEMBER_VOTE_KEY_FORMAT, roomId, memberId);
    }

    private String getCandidateVoteKey(String roomId, long stationId, VoteStatus status) {
        return String.format(CANDIDATE_VOTE_KEY_FORMAT, roomId, stationId, status.getName());
    }
}
