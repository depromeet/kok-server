package com.kok.kokapi.room.application.service;

import com.kok.kokcore.room.application.port.out.SaveRoomPort;
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
    public Room createRoom(String roomName, int capacity, String hostProfile, String password) {
        Room room = Room.create(roomName, capacity, hostProfile, password);
        Room savedRoom = saveRoomPort.save(room);
        joinRoomUseCase.addParticipant(savedRoom.getId(), savedRoom.getHostProfile());
        return savedRoom;
    }
}
