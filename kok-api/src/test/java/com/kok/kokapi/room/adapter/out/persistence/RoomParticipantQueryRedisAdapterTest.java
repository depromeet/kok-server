package com.kok.kokapi.room.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import com.kok.kokapi.common.template.RepositoryTest;
import com.kok.kokapi.fixture.MemberFixture;
import com.kok.kokapi.fixture.RoomFixture;
import com.kok.kokcore.room.application.port.out.SaveRoomParticipantsPort;
import com.kok.kokcore.room.application.port.out.SaveRoomPort;
import com.kok.kokcore.room.domain.Member;
import com.kok.kokcore.room.domain.Room;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class RoomParticipantQueryRedisAdapterTest extends RepositoryTest {

    @Autowired
    private SaveRoomPort saveRoomPort;
    @Autowired
    private SaveRoomParticipantsPort saveRoomParticipantsPort;
    @Autowired
    private RoomParticipantQueryRedisAdapter roomParticipantQueryRedisAdapter;

    @DisplayName("현재 약속방에 참여한 인원을 반환한다.")
    @Test
    void countParticipants() {
        // given
        Member member = MemberFixture.createLeader();
        Room room = saveRoomPort.save(RoomFixture.create(3, member));
        Member member2 = MemberFixture.createFollower();
        saveRoomParticipantsPort.joinRoom(room.getId(), member);
        saveRoomParticipantsPort.joinRoom(room.getId(), member2);

        // when
        long participants = roomParticipantQueryRedisAdapter.countParticipantsByRoomId(room.getId());

        // then
        assertThat(participants).isEqualTo(2);
    }
}