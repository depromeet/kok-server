package com.kok.kokapi.vote.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.kok.kokapi.common.template.ServiceTest;
import com.kok.kokapi.fixture.MemberFixture;
import com.kok.kokapi.room.adapter.out.persistence.RoomParticipantSaveAdapter;
import com.kok.kokapi.room.adapter.out.persistence.RoomSaveRedisAdapter;
import com.kok.kokapi.vote.adapter.out.persistence.CandidateCommandRedisAdapter;
import com.kok.kokapi.vote.adapter.out.persistence.VoteKey;
import com.kok.kokcore.room.domain.Member;
import com.kok.kokcore.room.domain.Room;
import com.kok.kokcore.vote.domain.Candidate;
import com.kok.kokcore.vote.domain.Vote;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

class VoteServiceTest extends ServiceTest {

    @Autowired
    private VoteService voteService;
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
        // when
        voteService.saveVotes(room.getId(), member.getMemberId(), List.of(1L));

        // then
        String memberKey = VoteKey.memberKey(room.getId(), member.getMemberId());
        String votedMembersKey = VoteKey.votedMembersOfStationKey(
            new Vote(candidate, member.getMemberId()));

        Set<Object> storedVotes = redisTemplate.opsForSet().members(memberKey);
        Set<Object> votedMemberIds = redisTemplate.opsForSet().members(votedMembersKey);

        assertAll(
            () -> assertThat(storedVotes).containsExactlyInAnyOrder(1),
            () -> assertThat(votedMemberIds).containsExactlyInAnyOrder(member.getMemberId())
        );
    }

    @DisplayName("사용자 투표 정보를 저장하기 전에 이전 투표 내역을 삭제한다.")
    @Test
    void initiateBeforeSaveVote() {
        // given
        voteService.saveVotes(room.getId(), member.getMemberId(), List.of(1L));

        // when
        voteService.saveVotes(room.getId(), member.getMemberId(), List.of(2L, 3L));

        // then
        String memberKey = VoteKey.memberKey(room.getId(), member.getMemberId());
        String votedMembersKey = VoteKey.votedMembersOfStationKey(
            new Vote(room.getId(), 1L, member.getMemberId()));

        Long memberSize = redisTemplate.opsForSet().size(memberKey);
        Long votedMembersSize = redisTemplate.opsForSet().size(votedMembersKey);

        assertAll(
            () -> assertThat(memberSize).isEqualTo(2),
            () -> assertThat(votedMembersSize).isZero()
        );
    }

    @DisplayName("사용자가 투표를 완료했으면 true를 반환한다.")
    @Test
    void isVotedByMember() {
        // given
        voteService.saveVotes(room.getId(), member.getMemberId(), List.of(1L));

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
        voteService.saveVotes(room.getId(), member.getMemberId(), List.of(1L));
        voteService.saveVotes(room.getId(), member2.getMemberId(), List.of(2L));

        // when
        int count = voteService.countVotedMembers(room.getId());

        // then
        assertThat(count).isEqualTo(2);
    }

    @DisplayName("사용자의 투표 정보를 반환한다.")
    @Test
    void getVotesByMember() {
        // given
        voteService.saveVotes(room.getId(), member.getMemberId(), List.of(1L));

        // when
        List<Vote> result = voteService.getVotesByMember(room.getId(), member.getMemberId());

        // then
        assertThat(result).hasSize(1)
            .containsExactlyInAnyOrder(new Vote(room.getId(), 1L, member.getMemberId()));
    }

    @DisplayName("특정 투표에 참여한 사용자의 정보를 반환한다.")
    @Test
    void getMembersByVote() {
        voteService.saveVotes(room.getId(), member.getMemberId(), List.of(1L));
        voteService.saveVotes(room.getId(), member2.getMemberId(), List.of(1L));

        Vote vote = new Vote(candidate, member.getMemberId());

        List<Member> result = voteService.getMembersByVote(vote);

        assertThat(result).hasSize(2)
            .extracting(Member::getMemberId)
            .containsExactlyInAnyOrder(member.getMemberId(), member2.getMemberId());
    }

    @DisplayName("방이 투표 상태가 아니면 예외가 발생한다.")
    @Test
    void throwExceptionWhenRoomNotInVoteStatus() {
        Room locationInputRoom = Room.create("inputRoom", 3, member);
        roomSaveRedisAdapter.save(locationInputRoom);

        assertThatThrownBy(() -> voteService.countVotedMembers(locationInputRoom.getId()))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("Room is not on vote status");
    }

    @DisplayName("방에 속하지 않은 멤버가 투표하면 예외가 발생한다.")
    @Test
    void throwExceptionWhenMemberNotInRoom() {
        String nonParticipantId = "unknown";

        assertThatThrownBy(() -> voteService.saveVotes(room.getId(), nonParticipantId, List.of()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Member not found with id");
    }

    @DisplayName("아직 투표하지 않은 사용자가 투표 내역 조회 시 예외가 발생한다.")
    @Test
    void throwExceptionWhenGetVotesWithoutVoting() {
        assertThatThrownBy(() -> voteService.getVotesByMember(room.getId(), member.getMemberId()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Not voted by member with id");
    }
}
