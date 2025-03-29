package com.kok.kokcore.room.usecase;

import java.time.LocalDateTime;

public interface UpdateRoomUseCase {

    void updateRoomVoteDeadline(String roomId, LocalDateTime current);
}
