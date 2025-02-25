package com.kok.kokcore.room.domain;

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
        String password = "secret";

        // When
        Room room = Room.create(roomName, capacity, hostProfile, password);

        // Then
        assertNotNull(room);
        assertNotNull(room.getId());
        assertEquals(roomName, room.getRoomName());
        assertEquals(capacity, room.getCapacity());
        assertEquals(hostProfile, room.getHostProfile());
        assertEquals(password, room.getPassword());
    }

    @DisplayName("약속방 생성 실패 - 약속방 이름이 없는 경우 약속방이 생성되지 않는다.")
    @Test
    void createRoomWithEmptyRoomName() {
        // Given
        String roomName = " ";
        int capacity = 4;
        String hostProfile = "hostProfile";
        String password = "secret";

        // When & Then
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> Room.create(roomName, capacity, hostProfile, password));
        assertTrue(exception.getMessage().contains("Room name is required"));
    }

    @DisplayName("약속방 생성 실패 - 최소 요구 인원(2명) 미만")
    @Test
    void createRoomWithInvalidCapacity() {
        // Given
        String roomName = "Test Room";
        int capacity = 1;
        String hostProfile = "hostProfile";
        String password = "secret";

        // When & Then
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> Room.create(roomName, capacity, hostProfile, password));
        assertTrue(exception.getMessage().contains("At least 2 participants are required"));
    }

    @DisplayName("약속방 생성 실패 - 방장 프로필 입력되지 않음")
    @Test
    void createRoomWIthEmptyHostProfile() {
        // Given
        String roomName = "Test Room";
        int capacity = 4;
        String hostProfile = " ";
        String password = "secret";

        // When & Then
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> Room.create(roomName, capacity, hostProfile, password));
        assertTrue((exception.getMessage().contains("Host profile is required")));
    }
}