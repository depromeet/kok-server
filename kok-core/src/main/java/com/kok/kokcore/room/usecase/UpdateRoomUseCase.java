package com.kok.kokcore.room.usecase;

import java.time.LocalDateTime;

public interface UpdateRoomUseCase {

    void startVoteIfLocationInputEnded(String roomId, LocalDateTime current);
}
