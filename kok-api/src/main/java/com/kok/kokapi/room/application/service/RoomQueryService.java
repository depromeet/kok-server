package com.kok.kokapi.room.application.service;

import com.kok.kokcore.room.application.port.out.LoadRoomPort;
import com.kok.kokcore.room.domain.Room;
import com.kok.kokcore.room.usecase.GetRoomUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomQueryService implements GetRoomUseCase {

    private final LoadRoomPort loadRoomPort;

    @Override
    public Room findRoomById(String roomId) {
        return loadRoomPort.findRoomById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found with id: " + roomId));
    }
}
