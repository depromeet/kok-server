package com.kok.kokapi.room.application.service;

import com.kok.kokapi.common.config.ServiceTestConfig;
import com.kok.kokcore.room.application.port.out.SaveRoomPort;
import com.kok.kokcore.room.domain.Room;
import com.kok.kokcore.room.usecase.JoinRoomUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Import({ServiceTestConfig.class})
@SpringBootTest(classes = {RoomCreationService.class})
class RoomCreationServiceTest {
    private SaveRoomPort saveRoomPort;
    private JoinRoomUseCase joinRoomUseCase;

    @Autowired
    private RoomCreationService roomCreationService;

    @BeforeEach
    void setUp() {
        saveRoomPort = mock(SaveRoomPort.class);
        joinRoomUseCase = mock(JoinRoomUseCase.class);
        roomCreationService = new RoomCreationService(saveRoomPort, joinRoomUseCase);
    }

    @DisplayName("약속방이 정상적으로 생성된다.")
    @Test
    void createRoom() {
        String roomName = "Test Room";
        int capacity = 4;
        String hostProfile = "hostProfile";
        String password = "secret";

        when(saveRoomPort.save(any(Room.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Room createdRoom = roomCreationService.createRoom(roomName, capacity, hostProfile, password);

        assertNotNull(createdRoom);
        assertNotNull(createdRoom.getId());
        assertEquals(roomName, createdRoom.getRoomName());
        assertEquals(capacity, createdRoom.getCapacity());
        assertEquals(hostProfile, createdRoom.getHostProfile());
        assertEquals(password, createdRoom.getPassword());
    }
}