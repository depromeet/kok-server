package com.kok.kokapi.vote.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.kok.kokapi.common.template.ServiceTest;
import com.kok.kokcore.vote.domain.Candidate;
import com.kok.kokcore.vote.domain.Vote;
import com.kok.kokcore.vote.domain.vo.VoteStatus;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

class VoteServiceTest extends ServiceTest {

    private static final String VOTES_KEY = "vote:";
    private static final String CANDIDATE_KEY = "candidate:";
    private static final String MEMBER_KEY = "member:";

    @Autowired
    private VoteService voteService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @DisplayName("사용자 투표 정보를 후보별/사용자별로 모두 저장한다.")
    @Test
    void saveVotesToRedis() {
        // given
        String roomId = "roomId";
        String memberId = "memberTest";
        List<Vote> votes = List.of(
            new Vote(new Candidate(roomId, 1), memberId, VoteStatus.AGREE),
            new Vote(new Candidate(roomId, 2), memberId, VoteStatus.DISAGREE)
        );
        String memberKey = getMemberKey(roomId, memberId);
        String agreeCandidateKey = getCandidateKey(roomId, 1, VoteStatus.AGREE);
        String disagreeCandidateKey = getCandidateKey(roomId, 2, VoteStatus.DISAGREE);

        // when
        voteService.saveVotes(votes);

        // then
        Map<Object, Object> storedVotes = redisTemplate.opsForHash().entries(memberKey);
        Set<Object> agreeMemberIds = redisTemplate.opsForSet().members(agreeCandidateKey);
        Set<Object> disagreeMemberIds = redisTemplate.opsForSet().members(disagreeCandidateKey);

        assertAll(
            () -> assertThat(storedVotes).containsEntry(1L, VoteStatus.AGREE.getName()),
            () -> assertThat(storedVotes).containsEntry(2L, VoteStatus.DISAGREE.getName()),
            () -> assertThat(agreeMemberIds).containsExactlyInAnyOrder(memberId),
            () -> assertThat(disagreeMemberIds).containsExactlyInAnyOrder(memberId)
        );
    }

    private String getCandidateKey(String roomId, long stationId, VoteStatus voteStatus) {
        return new StringJoiner(":")
            .add(VOTES_KEY + roomId)
            .add(CANDIDATE_KEY + stationId)
            .add(voteStatus.getName())
            .toString();
    }

    private String getMemberKey(String roomId, String memberId) {
        return new StringJoiner(":")
            .add(VOTES_KEY + roomId)
            .add(MEMBER_KEY + memberId)
            .toString();
    }
}

