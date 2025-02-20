package com.kok.kokcore.application.port.out;

import com.kok.kokcore.domain.Room;

public interface RoomRepository {
    Room save(Room room);
    Room findById(String id);
}
