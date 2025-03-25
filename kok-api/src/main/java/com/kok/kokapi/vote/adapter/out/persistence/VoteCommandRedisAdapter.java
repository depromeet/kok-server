package com.kok.kokapi.vote.adapter.out.persistence;

import com.kok.kokcore.vote.application.port.out.DeleteVotePort;
import com.kok.kokcore.vote.application.port.out.SaveVotePort;
import com.kok.kokcore.vote.domain.Vote;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class VoteCommandRedisAdapter implements SaveVotePort, DeleteVotePort {

    private static final String MEMBER_VOTE_KEY_FORMAT = "vote:%s:member:%s";
    private static final String CANDIDATE_VOTE_KEY_FORMAT = "vote:%s:candidate:%d:%s";

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void saveByCandidate(Vote vote) {
        String key = getCandidateVoteKey(vote);
        redisTemplate.opsForSet().add(key, vote.getMemberId());
    }

    @Override
    public void saveAllByMember(List<Vote> votes) {
        validate(votes);
        String roomId = votes.getFirst().getRoomId();
        String memberId = votes.getFirst().getMemberId();
        String key = getMemberVoteKey(roomId, memberId);
        Map<Long, String> value = votes.stream()
            .collect(Collectors.toMap(
                Vote::getStationId,
                vote -> vote.getVoteStatus().getName()
            ));
        redisTemplate.opsForHash().putAll(key, value);
    }

    private void validate(List<Vote> votes) {
        if (Objects.isNull(votes) || votes.isEmpty()) {
            throw new IllegalArgumentException("No votes to save");
        }
    }

    @Override
    public void deleteByCandidate(Vote vote) {
        String key = getCandidateVoteKey(vote);
        redisTemplate.opsForSet().remove(key, vote.getMemberId());
    }

    @Override
    public void deleteAllByRoomIdAndMemberId(String roomId, String memberId) {
        String key = getMemberVoteKey(roomId, memberId);
        redisTemplate.delete(key);
    }

    private String getMemberVoteKey(String roomId, String memberId) {
        return String.format(MEMBER_VOTE_KEY_FORMAT, roomId, memberId);
    }

    private String getCandidateVoteKey(Vote vote) {
        return String.format(CANDIDATE_VOTE_KEY_FORMAT, vote.getRoomId(), vote.getStationId(),
            vote.getVoteStatus().getName());
    }
}
