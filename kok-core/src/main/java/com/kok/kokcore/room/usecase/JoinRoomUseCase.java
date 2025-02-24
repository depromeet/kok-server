package com.kok.kokcore.room.usecase;

import java.util.List;

public interface JoinRoomUseCase {
    void addParticipant(String roomId, String profile);
    List<String> getParticipants(String roomId);
}
