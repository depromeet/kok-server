package com.kok.kokapi.vote.adapter.out.persistence;

import com.kok.kokcore.vote.application.port.out.SaveVotePort;
import com.kok.kokcore.vote.domain.Vote;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class VoteCommandRedisAdapter implements SaveVotePort {

    private static final String VOTES_KEY = "vote:";
    private static final String CANDIDATE_KEY = "candidate:";

    public final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void saveAll(List<Vote> votes) {
        validate(votes);
        List<Vote> agreeVotes = getAgreeVotes(votes);
        List<Vote> disagreeVotes = getDisagreeVotes(votes);
        save(agreeVotes);
        save(disagreeVotes);
    }

    private void validate(List<Vote> votes) {
        if (Objects.isNull(votes) || votes.isEmpty()) {
            throw new IllegalArgumentException("No votes to save");
        }
    }

    private List<Vote> getAgreeVotes(List<Vote> votes) {
        return votes.stream().filter(vote -> vote.getVoteStatus().isAgree()).toList();
    }

    private List<Vote> getDisagreeVotes(List<Vote> votes) {
        return votes.stream().filter(vote -> vote.getVoteStatus().isDisagree()).toList();
    }

    private void save(List<Vote> agreeVotes) {
        String key = getKey(agreeVotes.getFirst());
        Object[] memberIds = agreeVotes.stream().map(Vote::getMemberId).toArray();
        redisTemplate.opsForSet().add(key, memberIds);
    }

    private String getKey(Vote vote) {
        StringJoiner joiner = new StringJoiner(":");
        joiner.add(VOTES_KEY + vote.getRoomId());
        joiner.add(CANDIDATE_KEY + vote.getStationId());
        joiner.add(vote.getVoteStatus().getName());
        return joiner.toString();
    }
}
