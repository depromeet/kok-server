package com.kok.kokcore.application.port.out;

import com.kok.kokcore.domain.Room;

import java.util.Optional;

public interface LoadRoomPort {
    Optional<Room> findRoomById(String roomId);
}
