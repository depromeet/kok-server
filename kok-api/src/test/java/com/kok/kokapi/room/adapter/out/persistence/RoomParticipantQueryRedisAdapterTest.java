package com.kok.kokapi.room.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import com.kok.kokapi.common.template.RepositoryTest;
import com.kok.kokapi.fixture.MemberFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class RoomParticipantQueryRedisAdapterTest extends RepositoryTest {

    @Autowired
    private RoomParticipantQueryRedisAdapter roomParticipantQueryRedisAdapter;
    @Autowired
    private RoomParticipantSaveAdapter roomParticipantSaveAdapter;

    @DisplayName("방의 참여 인원수를 반환한다.")
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
}
