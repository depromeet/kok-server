package com.kok.kokcore.room.usecase;

import com.kok.kokcore.room.domain.Room;

public interface GetRoomUseCase {
    Room getRoomById(String roomId);
}
