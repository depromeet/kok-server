package com.kok.kokcore.room.usecase;

import com.kok.kokcore.room.domain.Room;
import java.time.LocalDateTime;

public interface UpdateRoomUseCase {

    void startVote(String roomId, LocalDateTime current);

    void closeVote(String roomId);

    Room updateRoomStatus(String roomId, LocalDateTime current);
}
