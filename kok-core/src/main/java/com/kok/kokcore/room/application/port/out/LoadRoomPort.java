package com.kok.kokcore.room.application.port.out;

import com.kok.kokcore.room.domain.Room;

import java.util.Optional;

public interface LoadRoomPort {
    Optional<Room> findRoomById(String roomId);
}
