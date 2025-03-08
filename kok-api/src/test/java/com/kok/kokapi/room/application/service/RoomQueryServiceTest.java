package com.kok.kokapi.room.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.kok.kokapi.common.template.ServiceTest;
import com.kok.kokcore.room.application.port.out.SaveRoomPort;
import com.kok.kokcore.room.domain.Member;
import com.kok.kokcore.room.domain.Room;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class RoomQueryServiceTest extends ServiceTest {

    @Autowired
    private SaveRoomPort saveRoomPort;
    @Autowired
    private RoomQueryService roomQueryService;

    @DisplayName("존재하는 방의 정보를 조회한다.")
    @Test
    void findRoomById() {
        // given
        Member member = new Member("nickname", "image", "Leader");
        Room room = saveRoomPort.save(Room.create("room", 2, member, "1234"));

        // when
        Room result = roomQueryService.findRoomById(room.getId());

        // then
        assertThat(result).isEqualTo(room);
    }

    @DisplayName("존재하지 않는 방의 정보를 조회하려고 하면 예외가 발생한다.")
    @Test
    void cannotFindRoomById() {
        // given
        String roomId = "unknownId";

        // when & then
        assertThatThrownBy(() -> roomQueryService.findRoomById(roomId))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Room not found with id: " + roomId);
    }
}
