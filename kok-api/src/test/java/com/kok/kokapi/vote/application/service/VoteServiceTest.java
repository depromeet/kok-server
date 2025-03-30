package com.kok.kokapi.vote.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.kok.kokapi.common.template.ServiceTest;
import com.kok.kokcore.vote.domain.Candidate;
import com.kok.kokcore.vote.domain.Vote;
import com.kok.kokcore.vote.domain.vo.VoteStatus;
import com.kok.kokcore.vote.port.out.SaveVotePort;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

class VoteServiceTest extends ServiceTest {

    private static final String MEMBER_VOTE_KEY_FORMAT = "vote:%s:member:%s";
    private static final String CANDIDATE_VOTE_KEY_FORMAT = "vote:%s:candidate:%d:%s";

    @Autowired
    private VoteService voteService;
    @Autowired
    private SaveVotePort saveVotePort;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @DisplayName("사용자 투표 정보를 후보별/사용자별로 모두 저장한다.")
    @Test
    void saveVotes() {
        // given
        String roomId = "roomId";
        String memberId = "memberId";
        List<Vote> votes = List.of(
            new Vote(new Candidate(roomId, 1), memberId, VoteStatus.AGREE),
            new Vote(new Candidate(roomId, 2), memberId, VoteStatus.DISAGREE)
        );
        String memberKey = getMemberVoteKey(roomId, memberId);
        String agreeCandidateKey = getCandidateVoteKey(roomId, 1, VoteStatus.AGREE);
        String disagreeCandidateKey = getCandidateVoteKey(roomId, 2, VoteStatus.DISAGREE);

        // when
        voteService.saveVotes(roomId, memberId, List.of(1L, 2L));

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

    @DisplayName("사용자 투표 정보를 저장하기 전에 이전 투표 내역을 삭제한다.")
    @Test
    void initiateBeforeSaveVote() {
        // given
        String roomId = "roomId";
        String memberId = "memberId";
        Candidate candidate = new Candidate(roomId, 1);
        Candidate candidate2 = new Candidate(roomId, 2);
        List<Vote> votes = List.of(
            new Vote(candidate, memberId, VoteStatus.AGREE),
            new Vote(candidate2, memberId, VoteStatus.DISAGREE)
        );
        saveVotePort.saveAllByMember(votes);
        for (Vote vote : votes) {
            saveVotePort.saveByCandidate(vote);
        }

        // when
        voteService.saveVotes(roomId, memberId, List.of());

        // then
        Long storedVoteCount = redisTemplate.opsForHash()
            .size(getMemberVoteKey(roomId, memberId));
        Long agreeCountForStation1 = redisTemplate.opsForSet()
            .size(getCandidateVoteKey(roomId, 1, VoteStatus.AGREE));
        Long disagreeCountForStation1 = redisTemplate.opsForSet()
            .size(getCandidateVoteKey(roomId, 1, VoteStatus.DISAGREE));
        Long disagreeCountForStation2 = redisTemplate.opsForSet()
            .size(getCandidateVoteKey(roomId, 2, VoteStatus.DISAGREE));
        assertAll(
            () -> assertThat(storedVoteCount).isEqualTo(2),
            () -> assertThat(agreeCountForStation1).isZero(),
            () -> assertThat(disagreeCountForStation1).isEqualTo(1),
            () -> assertThat(disagreeCountForStation2).isEqualTo(1)
        );
    }

    @DisplayName("사용자가 투표를 완료했으면 true를 반환한다.")
    @Test
    void isVotedByMember() {
        // given
        String roomId = "roomId";
        String memberId = "memberId";
        Candidate candidate = new Candidate(roomId, 1);
        List<Vote> votes = List.of(new Vote(candidate, memberId, VoteStatus.AGREE));
        saveVotePort.saveAllByMember(votes);
        for (Vote vote : votes) {
            saveVotePort.saveByCandidate(vote);
        }

        // when
        boolean result = voteService.isVotedByMember(roomId, memberId);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("사용자가 투표를 완료하지 않았으면 false를 반환한다.")
    @Test
    void isNotVotedByMember() {
        // given
        String roomId = "roomId";
        String memberId = "memberId";

        // when
        boolean result = voteService.isVotedByMember(roomId, memberId);

        // then
        assertThat(result).isFalse();
    }

    private String getMemberVoteKey(String roomId, String memberId) {
        return String.format(MEMBER_VOTE_KEY_FORMAT, roomId, memberId);
    }

    private String getCandidateVoteKey(String roomId, long stationId, VoteStatus status) {
        return String.format(CANDIDATE_VOTE_KEY_FORMAT, roomId, stationId, status.getName());
    }
}

