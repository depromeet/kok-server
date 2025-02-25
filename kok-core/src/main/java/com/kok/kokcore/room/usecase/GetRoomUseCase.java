package com.kok.kokcore.room.usecase;

import com.kok.kokcore.room.domain.Room;

public interface GetRoomUseCase {
    /**
     * roomId로 약속방을 조회한다.
     *
     * @param roomId
     * @return 조회된 Room 도메인 객체
     */
    Room findRoomById(String roomId);
}
