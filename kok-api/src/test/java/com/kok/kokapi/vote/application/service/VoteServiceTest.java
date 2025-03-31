package com.kok.kokapi.vote.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
    private Member member2;
    private Candidate candidate;
    private Candidate candidate2;

    @BeforeEach
    void init() {
        member = MemberFixture.createLeader();
        member2 = MemberFixture.createFollower();
        room = Room.create("room", 3, member);
        room.startVote();
        roomSaveRedisAdapter.save(room);
        roomParticipantSaveAdapter.joinRoom(room.getId(), member);
        roomParticipantSaveAdapter.joinRoom(room.getId(), member2);
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

    @DisplayName("방에 투표를 완료한 인원 수를 반환한다.")
    @Test
    void countVotedMembers() {
        // given
        List<Vote> votes = List.of(new Vote(candidate, member.getMemberId(), VoteStatus.AGREE));
        List<Vote> votes2 = List.of(new Vote(candidate, member2.getMemberId(), VoteStatus.AGREE));
        saveVotes(votes);
        saveVotes(votes2);

        // when
        int count = voteService.countVotedMembers(room.getId());

        // then
        assertThat(count).isEqualTo(2);
    }

    @DisplayName("사용자의 투표 정보를 반환한다.")
    @Test
    void getVotesByMember() {
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
        List<Vote> result = voteService.getVotesByMember(room.getId(), member.getMemberId());

        // then
        assertThat(result).hasSize(2)
            .extracting("stationId", "voteStatus")
            .containsExactlyInAnyOrder(
                org.assertj.core.api.Assertions.tuple(1L, VoteStatus.AGREE),
                org.assertj.core.api.Assertions.tuple(2L, VoteStatus.DISAGREE)
            );
    }

    @DisplayName("특정 투표에 참여한 사용자의 정보를 반환한다.")
    @Test
    void getMembersByVote() {
        // given
        Vote vote = new Vote(candidate, member.getMemberId(), VoteStatus.AGREE);
        List<Vote> votes = List.of(vote);
        List<Vote> votes2 = List.of(new Vote(candidate, member2.getMemberId(), VoteStatus.AGREE));
        saveVotes(votes);
        saveVotes(votes2);

        // when
        List<Member> result = voteService.getMembersByVote(vote);

        // then
        assertThat(result).hasSize(2)
            .extracting(Member::getMemberId)
            .containsExactlyInAnyOrder(member.getMemberId(), member2.getMemberId());
    }

    private void saveVotes(List<Vote> votes) {
        saveVotePort.saveAllByMember(votes);
        for (Vote vote : votes) {
            saveVotePort.saveByCandidate(vote);
        }
    }

    @DisplayName("방이 투표 상태가 아니면 예외가 발생한다.")
    @Test
    void throwExceptionWhenRoomNotInVoteStatus() {
        // given
        Room locationInputRoom = Room.create("inputRoom", 3, member);
        roomSaveRedisAdapter.save(locationInputRoom);

        // when & then
        assertThatThrownBy(() -> voteService.countVotedMembers(locationInputRoom.getId()))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("Room is not on vote status");
    }

    @DisplayName("방에 속하지 않은 멤버가 투표하면 예외가 발생한다.")
    @Test
    void throwExceptionWhenMemberNotInRoom() {
        // given
        String nonParticipantId = "unknown";

        // when & then
        assertThatThrownBy(() -> voteService.saveVotes(room.getId(), nonParticipantId, List.of()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Cannot find member with id");
    }

    @DisplayName("아직 투표하지 않은 사용자가 투표 내역 조회 시 예외가 발생한다.")
    @Test
    void throwExceptionWhenGetVotesWithoutVoting() {
        // when & then
        assertThatThrownBy(() -> voteService.getVotesByMember(room.getId(), member.getMemberId()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Not voted by member with id");
    }

    private String getMemberVoteKey(String roomId, String memberId) {
        return String.format(MEMBER_VOTE_KEY_FORMAT, roomId, memberId);
    }

    private String getCandidateVoteKey(String roomId, long stationId, VoteStatus status) {
        return String.format(CANDIDATE_VOTE_KEY_FORMAT, roomId, stationId, status.getName());
    }
}

