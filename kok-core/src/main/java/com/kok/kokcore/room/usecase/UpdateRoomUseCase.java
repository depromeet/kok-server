package com.kok.kokcore.room.usecase;

import java.time.LocalDateTime;

public interface UpdateRoomUseCase {

    void startVote(String roomId, LocalDateTime current);
}
