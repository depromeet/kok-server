package com.kok.kokapi.vote.adapter.out.persistence;

import com.kok.kokapi.common.util.RedisExecutor;
import com.kok.kokcore.vote.domain.Vote;
import com.kok.kokcore.vote.port.out.DeleteVotePort;
import com.kok.kokcore.vote.port.out.SaveVotePort;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class VoteCommandRedisAdapter implements SaveVotePort, DeleteVotePort {

    private static final Duration VOTE_TTL = Duration.ofDays(3);

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void saveVotedStationsByRoomIdAndMemberId(List<Long> stationIds, String roomId,
        String memberId) {
        String memberKey = VoteKey.memberKey(roomId, memberId);
        RedisExecutor.runOrThrow("saveVoteMemberHash", () -> {
            for (Long stationId : stationIds) {
                redisTemplate.opsForSet().add(memberKey, stationId);
            }
            redisTemplate.expire(memberKey, getTTL(memberKey));
        });
    }

    @Override
    public void saveVotedMemberSet(String roomId, String memberId) {
        String votedMemberSetKey = VoteKey.voteKey(roomId);
        RedisExecutor.runOrThrow("saveVotedMemberSet", () -> {
            redisTemplate.opsForSet().add(votedMemberSetKey, memberId);
            redisTemplate.expire(votedMemberSetKey, getTTL(votedMemberSetKey));
        });
    }

    @Override
    public void saveVotedMembersByRoomIdAndStationId(String memberId, String roomId,
        long stationId) {
        String key = VoteKey.votedMembersOfStationKey(roomId, stationId);
        RedisExecutor.runOrThrow("saveVoteStatusSet", () -> {
            redisTemplate.opsForSet().add(key, memberId);
            redisTemplate.expire(key, getTTL(key));
        });
    }

    @Override
    public void increaseVotedCountByRoomIdAndStationId(String roomId, long stationId) {
        String key = VoteKey.votedCountOfStationIdKey(roomId);
        RedisExecutor.runOrThrow("incrementVoteStatusCountZSet", () -> {
            redisTemplate.opsForZSet().incrementScore(key, stationId, 1);
            redisTemplate.expire(key, getTTL(key));
        });
    }

    @Override
    public void removeMemberFromVoteStatusSet(Vote vote) {
        String key = VoteKey.votedMembersOfStationKey(vote);
        RedisExecutor.runOrThrow("removeMemberFromVoteStatusSet", () ->
            redisTemplate.opsForSet().remove(key, vote.getMemberId())
        );
    }

    @Override
    public void decrementVoteCountInZSet(Vote vote) {
        String key = VoteKey.votedCountOfStationIdKey(vote);
        RedisExecutor.runOrThrow("decrementVoteCountInZSet", () ->
            redisTemplate.opsForZSet().incrementScore(key, vote.getStationId(), -1)
        );
    }

    @Override
    public void deleteVotesByRoomIdAndMemberId(String roomId, String memberId) {
        String key = VoteKey.memberKey(roomId, memberId);
        RedisExecutor.runOrThrow("deleteMemberVoteHash", () ->
            redisTemplate.delete(key)
        );
    }

    @Override
    public void removeMemberFromVotedSet(String roomId, String memberId) {
        String key = VoteKey.voteKey(roomId);
        RedisExecutor.runOrThrow("removeMemberFromVotedSet", () ->
            redisTemplate.opsForSet().remove(key, memberId)
        );
    }

    private Duration getTTL(String key) {
        Long expireSeconds = redisTemplate.getExpire(key);
        if (Objects.isNull(expireSeconds) || expireSeconds <= 0) {
            log.warn("Cannot find key: {}, initiate expire TTL", key);
            return VOTE_TTL;
        }
        return Duration.ofSeconds(expireSeconds);
    }
}
