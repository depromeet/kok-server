package com.kok.kokcore.room.domain;

import com.kok.kokcore.room.domain.vo.MemberRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoomTest {

    @DisplayName("약속방이 정상적으로 생성된다.")
    @Test
    void createRoom() {
        // Given
        String roomName = "Test Room";
        int capacity = 4;
        String hostProfile = "hostProfile";
        String hostNickname = "test";
        String password = "secret";
        Member host = new Member(hostNickname, hostProfile, MemberRole.LEADER);

        // When
        Room room = Room.create(roomName, capacity, host);

        // Then
        assertAll("약속방 생성",
                () -> assertNotNull(room.getId(), "ID는 null이 아니어야 합니다."),
                () -> assertEquals(roomName, room.getRoomName(), "방 이름이 일치해야 합니다."),
                () -> assertEquals(capacity, room.getCapacity(), "참여 인원 수가 일치해야 합니다."),
                () -> assertEquals(hostNickname, room.getMember().getNickname(), "방장 닉네임이 일치해야 합니다."),
                () -> assertEquals(hostProfile, room.getMember().getProfile(), "방장 프로필이 일치해야 합니다."),
                () -> assertEquals(MemberRole.LEADER, room.getMember().getRole(), "방장 역할은 Leader여야 합니다.")
        );
    }

    @DisplayName("약속방 생성 실패 - 약속방 이름이 없는 경우 약속방이 생성되지 않는다.")
    @Test
    void createRoomWithEmptyRoomName() {
        // Given
        String roomName = " ";
        int capacity = 4;
        String hostNickname = "hostNickname";
        String hostProfile = "hostProfile";
        String password = "secret";
        Member host = new Member(hostNickname, hostProfile, MemberRole.LEADER);

        // When & Then
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> Room.create(roomName, capacity, host));
        assertTrue(exception.getMessage().contains("Room name is required"));
    }

    @DisplayName("약속방 생성 실패 - 최소 요구 인원(2명) 미만")
    @Test
    void createRoomWithInvalidCapacity() {
        // Given
        String roomName = "Test Room";
        int capacity = 1;
        String hostNickname = "hostNickname";
        String hostProfile = "hostProfile";
        Member host = new Member(hostNickname, hostProfile, MemberRole.LEADER);

        // When & Then
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> Room.create(roomName, capacity, host));
        assertTrue(exception.getMessage().contains("At least 2 participants are required"));
    }
}
