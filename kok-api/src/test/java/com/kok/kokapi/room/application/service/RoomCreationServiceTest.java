package com.kok.kokapi.room.application.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.kok.kokapi.common.template.ServiceTest;
import com.kok.kokcore.room.domain.Member;
import com.kok.kokcore.room.domain.Room;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class RoomCreationServiceTest extends ServiceTest {

    @Autowired
    private RoomCreationService roomCreationService;

    @DisplayName("약속방이 정상적으로 생성된다.")
    @Test
    void createRoom() {
        String roomName = "Test Room";
        int capacity = 4;
        String hostNickname = "test";
        String hostProfile = "hostProfile";
        String password = "secret";
        Member host = new Member(hostNickname, hostProfile, "Leader");

        Room createdRoom = roomCreationService.createRoom(roomName, capacity, host, password);

        assertAll("Room Create Test",
                () -> assertNotNull(createdRoom, "Room 객체는 null이 아니어야 합니다."),
                () -> assertNotNull(createdRoom.getId(), "약속방 ID는 null이 아니어야 합니다."),
                () -> assertEquals(roomName, createdRoom.getRoomName(), "약속방 이름이 일치해야 합니다."),
                () -> assertEquals(capacity, createdRoom.getCapacity(), "참여 인원 수가 일치해야 합니다."),
                () -> assertEquals(password, createdRoom.getPassword(), "비밀번호가 일치해야 합니다."),
                () -> assertNotNull(createdRoom.getMember(), "방장 정보는 null이 아니어야 합니다."),
                () -> assertEquals(hostNickname, createdRoom.getMember().getNickname(), "방장 닉네임이 일치해야 합니다."),
                () -> assertEquals(hostProfile, createdRoom.getMember().getProfile(), "방장 프로필이 일치해야 합니다."),
                () -> assertEquals("Leader", createdRoom.getMember().getRole(), "방장 역할은 Leader여야 합니다.")
        );
    }
}
