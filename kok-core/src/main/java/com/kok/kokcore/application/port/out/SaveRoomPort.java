package com.kok.kokcore.application.port.out;

import com.kok.kokcore.domain.Room;

public interface SaveRoomPort {
    Room save(Room room);
}
