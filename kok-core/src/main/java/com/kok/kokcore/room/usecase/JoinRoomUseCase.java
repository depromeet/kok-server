package com.kok.kokcore.room.usecase;

import com.kok.kokcore.room.domain.Member;

public interface JoinRoomUseCase {
    void joinRoom(String roomId, Member member);
}
