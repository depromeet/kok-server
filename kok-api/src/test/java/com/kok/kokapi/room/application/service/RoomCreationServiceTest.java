package com.kok.kokapi.room.application.service;

import com.kok.kokapi.common.config.ServiceTestConfig;
import com.kok.kokcore.room.application.port.out.SaveRoomPort;
import com.kok.kokcore.room.domain.Member;
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
        String hostNickname = "test";
        String hostProfile = "hostProfile";
        String password = "secret";
        Member host = new Member(hostNickname, hostProfile, "Leader");

        when(saveRoomPort.save(any(Room.class))).thenAnswer(invocation -> invocation.getArgument(0));
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
