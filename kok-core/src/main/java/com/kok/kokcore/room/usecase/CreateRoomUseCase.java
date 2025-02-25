package com.kok.kokcore.room.usecase;

import com.kok.kokcore.room.domain.Room;

public interface CreateRoomUseCase {

    /**
     * 아래 정보로 새로운 약속방을 생성한다.
     *
     * @param roomName
     * @param capacity
     * @param hostProfile
     * @param password
     * @return 생성된 Room 도메인 객체
     */
    Room createRoom(String roomName, int capacity, String hostProfile, String password);
}
