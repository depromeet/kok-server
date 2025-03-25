package com.kok.kokapi.vote.adapter.out.persistence;

import com.kok.kokcore.vote.application.port.out.SaveVotePort;
import com.kok.kokcore.vote.domain.Vote;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class VoteCommandRedisAdapter implements SaveVotePort {

    private static final String VOTES_KEY = "vote:";
    private static final String CANDIDATE_KEY = "candidate:";
    private static final String MEMBER_KEY = "member:";

    public final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void saveByCandidate(Vote vote) {
        String key = getCandidateKey(vote);
        redisTemplate.opsForSet().add(key, vote.getMemberId());
    }

    @Override
    public void saveAllByMember(List<Vote> votes) {
        validate(votes);
        String key = getMemberKey(votes.getFirst());
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

    private String getCandidateKey(Vote vote) {
        StringJoiner joiner = new StringJoiner(":");
        joiner.add(VOTES_KEY + vote.getRoomId());
        joiner.add(CANDIDATE_KEY + vote.getStationId());
        joiner.add(vote.getVoteStatus().getName());
        return joiner.toString();
    }

    private String getMemberKey(Vote vote) {
        StringJoiner joiner = new StringJoiner(":");
        joiner.add(VOTES_KEY + vote.getRoomId());
        joiner.add(MEMBER_KEY + vote.getMemberId());
        return joiner.toString();
    }
}
