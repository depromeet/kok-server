package com.kok.kokapi.vote.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.kok.kokapi.common.template.ServiceTest;
import com.kok.kokapi.fixture.MemberFixture;
import com.kok.kokapi.room.adapter.out.persistence.RoomParticipantSaveAdapter;
import com.kok.kokapi.room.adapter.out.persistence.RoomSaveRedisAdapter;
import com.kok.kokapi.vote.adapter.out.persistence.CandidateCommandRedisAdapter;
import com.kok.kokcore.room.domain.Member;
import com.kok.kokcore.room.domain.Room;
import com.kok.kokcore.vote.domain.Candidate;
import com.kok.kokcore.vote.domain.Vote;
import com.kok.kokcore.vote.domain.vo.VoteStatus;
import com.kok.kokcore.vote.port.out.SaveVotePort;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
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
    @Autowired
    private RoomSaveRedisAdapter roomSaveRedisAdapter;
    @Autowired
    private RoomParticipantSaveAdapter roomParticipantSaveAdapter;
    @Autowired
    private CandidateCommandRedisAdapter candidateCommandRedisAdapter;

    private Room room;
    private Member member;
    private Candidate candidate;
    private Candidate candidate2;

    @BeforeEach
    void init() {
        member = MemberFixture.createLeader();
        room = Room.create("room", 3, member);
        roomSaveRedisAdapter.save(room);
        roomParticipantSaveAdapter.joinRoom(room.getId(), member);
        candidate = new Candidate(room.getId(), 1);
        candidate2 = new Candidate(room.getId(), 2);
        candidateCommandRedisAdapter.saveAll(List.of(candidate, candidate2));
    }

    @DisplayName("사용자 투표 정보를 후보별/사용자별로 모두 저장한다.")
    @Test
    void saveVotes() {
        // given
        String memberKey = getMemberVoteKey(room.getId(), member.getMemberId());
        String agreeCandidateKey = getCandidateVoteKey(room.getId(), 1, VoteStatus.AGREE);
        String disagreeCandidateKey = getCandidateVoteKey(room.getId(), 2, VoteStatus.DISAGREE);

        // when
        voteService.saveVotes(room.getId(), member.getMemberId(), List.of(1L));

        // then
        Map<Object, Object> storedVotes = redisTemplate.opsForHash().entries(memberKey);
        Set<Object> agreeMemberIds = redisTemplate.opsForSet().members(agreeCandidateKey);
        Set<Object> disagreeMemberIds = redisTemplate.opsForSet().members(disagreeCandidateKey);

        assertAll(
            () -> assertThat(storedVotes).containsEntry(1L, VoteStatus.AGREE.getName()),
            () -> assertThat(storedVotes).containsEntry(2L, VoteStatus.DISAGREE.getName()),
            () -> assertThat(agreeMemberIds).containsExactlyInAnyOrder(member.getMemberId()),
            () -> assertThat(disagreeMemberIds).containsExactlyInAnyOrder(member.getMemberId())
        );
    }

    @DisplayName("사용자 투표 정보를 저장하기 전에 이전 투표 내역을 삭제한다.")
    @Test
    void initiateBeforeSaveVote() {
        // given
        List<Vote> votes = List.of(
            new Vote(candidate, member.getMemberId(), VoteStatus.AGREE),
            new Vote(candidate2, member.getMemberId(), VoteStatus.DISAGREE)
        );
        saveVotePort.saveAllByMember(votes);
        for (Vote vote : votes) {
            saveVotePort.saveByCandidate(vote);
        }

        // when
        voteService.saveVotes(room.getId(), member.getMemberId(), List.of());

        // then
        Long storedVoteCount = redisTemplate.opsForHash()
            .size(getMemberVoteKey(room.getId(), member.getMemberId()));
        Long agreeCountForStation1 = redisTemplate.opsForSet()
            .size(getCandidateVoteKey(room.getId(), 1, VoteStatus.AGREE));
        Long disagreeCountForStation1 = redisTemplate.opsForSet()
            .size(getCandidateVoteKey(room.getId(), 1, VoteStatus.DISAGREE));
        Long disagreeCountForStation2 = redisTemplate.opsForSet()
            .size(getCandidateVoteKey(room.getId(), 2, VoteStatus.DISAGREE));
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
        Candidate candidate = new Candidate(room.getId(), 1);
        List<Vote> votes = List.of(new Vote(candidate, member.getMemberId(), VoteStatus.AGREE));
        saveVotePort.saveAllByMember(votes);
        for (Vote vote : votes) {
            saveVotePort.saveByCandidate(vote);
        }

        // when
        boolean result = voteService.isVotedByMember(room.getId(), member.getMemberId());

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("사용자가 투표를 완료하지 않았으면 false를 반환한다.")
    @Test
    void isNotVotedByMember() {
        // when
        boolean result = voteService.isVotedByMember(room.getId(), member.getMemberId());

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

