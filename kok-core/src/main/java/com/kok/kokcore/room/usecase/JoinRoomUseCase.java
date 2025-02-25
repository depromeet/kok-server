package com.kok.kokcore.room.usecase;

import java.util.List;

public interface JoinRoomUseCase {
    /**
     * 참여자가 약속방에 참여한다.
     * @param roomId
     * @param profile
     */
    void joinRoom(String roomId, String profile);

    /**
     * roomId로 조회된 약속방의 참여자 목록을 조회한다.
     * @param roomId
     * @return 참여자 프로필 목록
     */
    List<String> getParticipants(String roomId);
}
