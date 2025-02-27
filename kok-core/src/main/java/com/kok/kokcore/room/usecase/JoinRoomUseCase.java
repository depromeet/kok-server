package com.kok.kokcore.room.usecase;

public interface JoinRoomUseCase {
    /**
     * 참여자가 약속방에 참여한다.
     * @param roomId
     * @param profile
     */
    void joinRoom(String roomId, String profile);
}
