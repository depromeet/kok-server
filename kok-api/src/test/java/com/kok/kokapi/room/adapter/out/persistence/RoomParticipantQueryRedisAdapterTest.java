package com.kok.kokapi.room.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import com.kok.kokapi.common.template.RepositoryTest;
import com.kok.kokapi.fixture.MemberFixture;
import com.kok.kokcore.room.domain.Member;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class RoomParticipantQueryRedisAdapterTest extends RepositoryTest {

    @Autowired
    private RoomParticipantQueryRedisAdapter roomParticipantQueryRedisAdapter;

    @Autowired
    private RoomParticipantSaveAdapter roomParticipantSaveAdapter;

    @DisplayName("방의 참여 인원 수를 반환한다.")
    @Test
    void getParticipantCount() {
        // given
        String roomId = "roomId";
        roomParticipantSaveAdapter.joinRoom(roomId, MemberFixture.createLeader());

        // when
        Long participantCount = roomParticipantQueryRedisAdapter.countParticipantsById(roomId);

        // then
        assertThat(participantCount).isEqualTo(1);
    }

    @DisplayName("방의 모든 참여자 정보를 반환한다.")
    @Test
    void findMembersByRoomId() {
        // given
        String roomId = "roomId";
        Member member1 = MemberFixture.createLeader();
        Member member2 = MemberFixture.createFollower();
        roomParticipantSaveAdapter.joinRoom(roomId, member1);
        roomParticipantSaveAdapter.joinRoom(roomId, member2);

        // when
        List<Member> members = roomParticipantQueryRedisAdapter.findMembersByRoomId(roomId);

        // then
        assertThat(members)
            .extracting(Member::getMemberId)
            .containsExactlyInAnyOrder(member1.getMemberId(), member2.getMemberId());
    }

    @DisplayName("방 ID와 멤버 ID로 해당 멤버를 조회한다.")
    @Test
    void findByRoomIdAndMemberId() {
        // given
        String roomId = "roomId";
        Member member = MemberFixture.createLeader();
        roomParticipantSaveAdapter.joinRoom(roomId, member);

        // when
        Optional<Member> result = roomParticipantQueryRedisAdapter.findByRoomIdAndMemberId(
            roomId, member.getMemberId());

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getMemberId()).isEqualTo(member.getMemberId());
    }

    @DisplayName("존재하지 않는 멤버 ID로 조회하면 빈 Optional을 반환한다.")
    @Test
    void notFoundMemberByRoomIdAndMemberId() {
        // given
        String roomId = "roomId";
        Member member = MemberFixture.createLeader();
        roomParticipantSaveAdapter.joinRoom(roomId, member);

        // when
        Optional<Member> result = roomParticipantQueryRedisAdapter.findByRoomIdAndMemberId(
            roomId, "non-existent-id");

        // then
        assertThat(result).isEmpty();
    }
}
