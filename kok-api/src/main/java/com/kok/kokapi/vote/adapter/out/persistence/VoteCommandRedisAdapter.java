package com.kok.kokapi.vote.adapter.out.persistence;

import com.kok.kokapi.common.util.RedisExecutor;
import com.kok.kokcore.vote.domain.Vote;
import com.kok.kokcore.vote.port.out.DeleteVotePort;
import com.kok.kokcore.vote.port.out.SaveVotePort;
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

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void saveVoteMemberHash(List<Vote> votes) {
        validate(votes);
        String roomId = votes.getFirst().getRoomId();
        String memberId = votes.getFirst().getMemberId();
        String memberHashKey = VoteKey.memberKey(roomId, memberId);
        RedisExecutor.runOrThrow("saveVoteMemberHash", () -> {
            for (Vote vote : votes) {
                String field = VoteKey.stationField(vote.getStationId());
                String status = vote.getVoteStatus().getName();
                redisTemplate.opsForHash().put(memberHashKey, field, status);
            }
        });
    }

    @Override
    public void saveVotedMemberSet(String roomId, String memberId) {
        String votedMemberSetKey = VoteKey.voteKey(roomId);
        RedisExecutor.runOrThrow("saveVotedMemberSet", () ->
            redisTemplate.opsForSet().add(votedMemberSetKey, memberId)
        );
    }

    @Override
    public void saveVoteStatusSet(Vote vote) {
        String key = VoteKey.voteStatusMemberSetKey(vote);
        RedisExecutor.runOrThrow("saveVoteStatusSet", () ->
            redisTemplate.opsForSet().add(key, vote.getMemberId())
        );
    }

    @Override
    public void incrementVoteStatusCountZSet(Vote vote) {
        String key = VoteKey.voteStatusCountZSetKey(vote);
        RedisExecutor.runOrThrow("incrementVoteStatusCountZSet", () ->
            redisTemplate.opsForZSet().incrementScore(key, vote.getStationId(), 1)
        );
    }

    private void validate(List<Vote> votes) {
        if (Objects.isNull(votes) || votes.isEmpty()) {
            throw new IllegalArgumentException("No votes to save");
        }
    }

    @Override
    public void removeMemberFromVoteStatusSet(Vote vote) {
        String key = VoteKey.voteStatusMemberSetKey(vote);
        RedisExecutor.runOrThrow("removeMemberFromVoteStatusSet", () ->
            redisTemplate.opsForSet().remove(key, vote.getMemberId())
        );
    }

    @Override
    public void decrementVoteCountInZSet(Vote vote) {
        String key = VoteKey.voteStatusCountZSetKey(vote);
        RedisExecutor.runOrThrow("decrementVoteCountInZSet", () ->
            redisTemplate.opsForZSet().incrementScore(key, vote.getStationId(), -1)
        );
    }

    @Override
    public void deleteMemberVoteHash(String roomId, String memberId) {
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
}
