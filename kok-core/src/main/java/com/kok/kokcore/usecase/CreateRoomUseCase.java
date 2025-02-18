package com.kok.kokcore.usecase;

import com.kok.kokcore.domain.Room;

public interface CreateRoomUseCase {
    Room createRoom(String roomName, int capacity, String hostProfile, String password);
}
