package com.kok.kokcore.usecase;

import com.kok.kokcore.domain.Room;

public interface GetRoomUseCase {
    Room getRoomById(String roomId);
}
