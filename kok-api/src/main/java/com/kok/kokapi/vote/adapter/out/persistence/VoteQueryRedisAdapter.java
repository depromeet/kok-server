package com.kok.kokapi.vote.adapter.out.persistence;

import com.kok.kokapi.common.util.RedisExecutor;
import com.kok.kokcore.vote.domain.Vote;
import com.kok.kokcore.vote.domain.vo.VoteStatus;
import com.kok.kokcore.vote.port.out.LoadVotePort;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class VoteQueryRedisAdapter implements LoadVotePort {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean isExistsByRoomIdAndMemberId(String roomId, String memberId) {
        String key = VoteKey.memberKey(roomId, memberId);
        return RedisExecutor.runOrElseGet("isExistsByRoomIdAndMemberId", () ->
            !redisTemplate.opsForHash().entries(key).isEmpty(), false
        );
    }

    @Override
    public List<Vote> findAllByRoomIdAndMemberId(String roomId, String memberId) {
        String key = VoteKey.memberKey(roomId, memberId);
        return RedisExecutor.runOrElseGet("findAllByRoomIdAndMemberId", () -> {
            Map<Object, Object> voteInfos = redisTemplate.opsForHash().entries(key);
            List<Vote> votes = new ArrayList<>();
            for (Entry<Object, Object> voteInfo : voteInfos.entrySet()) {
                Long stationId = getStationId(voteInfo);
                String voteStatus = String.valueOf(voteInfo.getValue());
                votes.add(new Vote(roomId, stationId, memberId, voteStatus));
            }
            return votes;
        }, List.of());
    }

    @Override
    public int countMembersByRoomId(String roomId) {
        return RedisExecutor.runOrElseGet("countMembersByRoomId", () -> {
            String key = VoteKey.voteKey(roomId);
            Long count = redisTemplate.opsForSet().size(key);
            return Objects.nonNull(count) ? count.intValue() : 0;
        }, 0);
    }

    @Override
    public List<String> findMemberIdsByRoomIdAndStationIdAndStatus(
        String roomId, long stationId, VoteStatus voteStatus) {
        String key = VoteKey.voteStatusMemberSetKey(voteStatus, roomId, stationId);
        Set<Object> memberIds = RedisExecutor.runOrElseGet(
            "findMembersByRoomIdAndStationIdAndStatus",
            () -> redisTemplate.opsForSet().members(key), Set.of());
        return memberIds.stream().map(memberId -> (String) memberId).toList();
    }

    @Override
    public long getFirstStationIdByRoomIdAndVoteStatus(String roomId, VoteStatus voteStatus) {
        return RedisExecutor.runOrElseGet("getFirstStationIdByRoomIdAndVoteStatus", () -> {
            String key = VoteKey.voteStatusCountZSetKey(voteStatus, roomId);
            Set<ZSetOperations.TypedTuple<Object>> sorted =
                redisTemplate.opsForZSet().reverseRangeWithScores(key, 0, 0);

            if (Objects.isNull(sorted) || sorted.isEmpty()) {
                return -1L;
            }

            Object maxScoredStationId = sorted.iterator().next().getValue();
            return getStationId(String.valueOf(maxScoredStationId));
        }, -1L);
    }

    private Long getStationId(Entry<Object, Object> voteInfo) {
        return getStationId(voteInfo.getKey().toString());
    }

    private Long getStationId(String stationId) {
        try {
            return Long.valueOf(stationId);
        } catch (NumberFormatException e) {
            log.warn("Invalid stationId format in Redis: {}", stationId, e);
            throw new IllegalArgumentException("Invalid stationId format: " + stationId);
        }
    }
}
