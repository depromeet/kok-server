package com.kok.kokcore.room.port.out;

import com.kok.kokcore.room.domain.Room;

public interface SaveRoomPort {

    Room save(Room room);
}
