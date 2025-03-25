package com.kok.kokapi.vote.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.kok.kokapi.common.template.RepositoryTest;
import com.kok.kokcore.vote.domain.Candidate;
import com.kok.kokcore.vote.domain.Vote;
import com.kok.kokcore.vote.domain.vo.VoteStatus;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

class VoteCommandRedisAdapterTest extends RepositoryTest {

    private static final String MEMBER_VOTE_KEY_FORMAT = "vote:%s:member:%s";
    private static final String CANDIDATE_VOTE_KEY_FORMAT = "vote:%s:candidate:%d:%s";

    @Autowired
    private VoteCommandRedisAdapter voteCommandRedisAdapter;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @DisplayName("후보별로 사용자의 투표 정보를 저장한다.")
    @Test
    void saveVoteByCandidate() {
        // given
        String existingMemberId = "memberId2";
        String memberId = "memberId";
        Candidate candidate = new Candidate("roomId", 1);
        Vote vote = new Vote(candidate, memberId, VoteStatus.AGREE);
        String key = getCandidateVoteKey(vote);
        redisTemplate.opsForSet().add(key, existingMemberId);

        // when
        voteCommandRedisAdapter.saveByCandidate(vote);

        // then
        Set<Object> memberIds = redisTemplate.opsForSet().members(key);
        assertThat(memberIds).containsExactlyInAnyOrder(existingMemberId, memberId);
    }

    @DisplayName("사용자의 투표 결과를 저장한다.")
    @Test
    void saveAllByCandidateVotesOfMember() {
        // given
        String roomId = "roomId";
        String memberId = "memberId";
        Candidate candidate = new Candidate(roomId, 1);
        Candidate candidate2 = new Candidate(roomId, 2);
        Candidate candidate3 = new Candidate(roomId, 3);
        List<Vote> votes = List.of(
            new Vote(candidate, memberId, VoteStatus.AGREE),
            new Vote(candidate2, memberId, VoteStatus.DISAGREE),
            new Vote(candidate3, memberId, VoteStatus.DISAGREE)
        );

        // when
        voteCommandRedisAdapter.saveAllByMember(votes);

        // then
        Map<Object, Object> result = redisTemplate.opsForHash()
            .entries(getMemberVoteKey(roomId, memberId));
        assertThat(result).containsAllEntriesOf(
            Map.of(
                votes.get(0).getStationId(), votes.get(0).getVoteStatus().getName(),
                votes.get(1).getStationId(), votes.get(1).getVoteStatus().getName(),
                votes.get(2).getStationId(), votes.get(2).getVoteStatus().getName()
            )
        );
    }

    @DisplayName("후보별로 사용자의 투표 정보를 삭제한다.")
    @Test
    void deleteVoteByCandidate() {
        // given
        String memberId = "memberId";
        String memberId2 = "memberId2";
        Candidate candidate = new Candidate("roomId", 1);
        Vote vote = new Vote(candidate, memberId, VoteStatus.AGREE);
        String key = getCandidateVoteKey(vote);
        redisTemplate.opsForSet().add(key, memberId, memberId2);

        // when
        voteCommandRedisAdapter.deleteByCandidate(vote);

        // then
        Set<Object> memberIds = redisTemplate.opsForSet().members(key);
        assertThat(memberIds).containsExactlyInAnyOrder(memberId2);
    }

    @DisplayName("사용자의 투표 정보를 삭제한다.")
    @Test
    void deleteAllByRoomIdAndMemberId() {
        // given
        String roomId = "roomId";
        String memberId = "memberId";
        Candidate candidate = new Candidate(roomId, 1);
        Candidate candidate2 = new Candidate(roomId, 2);
        Candidate candidate3 = new Candidate(roomId, 3);
        String key = getMemberVoteKey(roomId, memberId);
        List<Vote> votes = List.of(
            new Vote(candidate, memberId, VoteStatus.AGREE),
            new Vote(candidate2, memberId, VoteStatus.DISAGREE),
            new Vote(candidate3, memberId, VoteStatus.DISAGREE)
        );
        redisTemplate.opsForHash().putAll(key, Map.of(
            votes.get(0).getStationId(), votes.get(0).getVoteStatus().getName(),
            votes.get(1).getStationId(), votes.get(1).getVoteStatus().getName(),
            votes.get(2).getStationId(), votes.get(2).getVoteStatus().getName()
        ));
        Long before = redisTemplate.opsForHash().size(key);

        // when
        voteCommandRedisAdapter.deleteAllByRoomIdAndMemberId(roomId, memberId);

        // then
        Long after = redisTemplate.opsForHash().size(key);
        assertAll(
            () -> assertThat(before).isEqualTo(3),
            () -> assertThat(after).isEqualTo(0),
            () -> assertThat(redisTemplate.hasKey(key)).isFalse()
        );
    }

    private String getCandidateVoteKey(Vote vote) {
        return String.format(CANDIDATE_VOTE_KEY_FORMAT, vote.getRoomId(), vote.getStationId(),
            vote.getVoteStatus().getName());
    }

    private String getMemberVoteKey(String roomId, String memberId) {
        return String.format(MEMBER_VOTE_KEY_FORMAT, roomId, memberId);
    }
}
