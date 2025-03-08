package com.kok.kokcore.room.usecase;

import com.kok.kokcore.room.domain.Member;
import com.kok.kokcore.room.domain.Room;

public interface CreateRoomUseCase {
    Room createRoom(String roomName, int capacity, Member host);
}