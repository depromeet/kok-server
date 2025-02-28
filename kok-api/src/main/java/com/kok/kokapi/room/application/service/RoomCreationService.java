package com.kok.kokapi.room.application.service;

import com.kok.kokcore.room.application.port.out.SaveRoomPort;
import com.kok.kokcore.room.domain.Member;
import com.kok.kokcore.room.domain.Room;
import com.kok.kokcore.room.usecase.CreateRoomUseCase;
import com.kok.kokcore.room.usecase.JoinRoomUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomCreationService implements CreateRoomUseCase {

    private final SaveRoomPort saveRoomPort;
    private final JoinRoomUseCase joinRoomUseCase;

    @Override
    public Room createRoom(String roomName, int capacity, Member host, String password) {
        Room room = Room.create(roomName, capacity, host, password);
        Room savedRoom = saveRoomPort.save(room);

        joinRoomUseCase.joinRoom(savedRoom.getId(), savedRoom.getMember());
        return savedRoom;
    }
}
