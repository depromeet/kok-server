package com.kok.kokapi.room.application.service;

import com.kok.kokcore.application.port.outbound.RoomRepository;
import com.kok.kokcore.domain.Room;
import com.kok.kokcore.usecase.CreateRoomUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomService implements CreateRoomUseCase {

    private final RoomRepository roomRepository;

    @Override
    public Room createRoom(String roomName, int capacity, String hostProfile, String password) {
        Room room = Room.create(roomName, capacity, hostProfile, password);
        return roomRepository.save(room);
    }
}
