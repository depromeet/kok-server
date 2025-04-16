package com.kok.kokapi.vote.adapter.out.persistence;

import com.kok.kokapi.common.util.RedisExecutor;
import com.kok.kokcore.vote.domain.Vote;
import com.kok.kokcore.vote.port.out.LoadVotePort;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class VoteQueryRedisAdapter implements LoadVotePort {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean isExistsByRoomIdAndMemberId(String roomId, String memberId) {
        String key = VoteKey.votedStationsByMemberKey(roomId, memberId);
        return RedisExecutor.runOrElseGet("isExistsByRoomIdAndMemberId", () ->
            !redisTemplate.opsForSet().members(key).isEmpty(), false
        );
    }

    @Override
    public List<Vote> findAllByRoomIdAndMemberId(String roomId, String memberId) {
        String key = VoteKey.votedStationsByMemberKey(roomId, memberId);
        return RedisExecutor.runOrElseGet("findAllByRoomIdAndMemberId", () -> {
            Set<Object> stationIds = redisTemplate.opsForSet().members(key);
            List<Vote> votes = new ArrayList<>();
            for (Object stationId : stationIds) {
                votes.add(new Vote(roomId, getStationId(stationId), memberId));
            }
            return votes;
        }, List.of());
    }

    @Override
    public int countVotedMembersByRoomId(String roomId) {
        return RedisExecutor.runOrElseGet("countMembersByRoomId", () -> {
            String key = VoteKey.voteCompletedMembersKey(roomId);
            Long count = redisTemplate.opsForSet().size(key);
            return Objects.nonNull(count) ? count.intValue() : 0;
        }, 0);
    }

    @Override
    public List<String> findMemberIdsByRoomIdAndStationId(String roomId, long stationId) {
        String key = VoteKey.votedMembersOfStationKey(roomId, stationId);
        Set<Object> memberIds = RedisExecutor.runOrElseGet(
            "findMembersByRoomIdAndStationIdAndStatus",
            () -> redisTemplate.opsForSet().members(key), Set.of());
        return memberIds.stream().map(memberId -> (String) memberId).toList();
    }

    @Override
    public long findFirstStationIdByRoomIdOrderByVotedCount(String roomId) {
        return RedisExecutor.runOrElseGet("getFirstStationIdByRoomIdAndVoteStatus", () -> {
            String key = VoteKey.votedScoreOfStationKey(roomId);
            Set<ZSetOperations.TypedTuple<Object>> sorted =
                redisTemplate.opsForZSet().reverseRangeWithScores(key, 0, 0);

            if (Objects.isNull(sorted) || sorted.isEmpty()) {
                return -1L;
            }

            Object maxScoredStationId = sorted.iterator().next().getValue();
            return getStationId(maxScoredStationId);
        }, -1L);
    }

    @Override
    public List<Long> findStationIdsByRoomIdOrderByVotedCount(String roomId) {
        return RedisExecutor.runOrElseGet("getStationIdsByRoomIdOrderByVotedCount", () -> {
            String key = VoteKey.votedScoreOfStationKey(roomId);
            Set<ZSetOperations.TypedTuple<Object>> sorted =
                redisTemplate.opsForZSet().reverseRangeWithScores(key, 0, -1);

            if (Objects.isNull(sorted) || sorted.isEmpty()) {
                return List.of();
            }

            return sorted.stream()
                .map(TypedTuple::getValue)
                .map(this::getStationId)
                .toList();
        }, List.of());
    }

    private Long getStationId(Object stationId) {
        try {
            return Long.valueOf(String.valueOf(stationId));
        } catch (NumberFormatException e) {
            log.warn("Invalid stationId format in Redis: {}", stationId, e);
            throw new IllegalArgumentException("Invalid stationId format: " + stationId);
        }
    }
}
