package com.kok.kokcore.room.usecase;

import com.kok.kokcore.room.domain.Room;

import java.util.List;

public interface GetRoomUseCase {
    /**
     * roomId로 약속방을 조회한다.
     *
     * @param roomId
     * @return 조회된 Room 도메인 객체
     */
    Room findRoomById(String roomId);

    /**
     * roomId로 조회된 약속방의 참여자 목록을 조회한다.
     * @param roomId
     * @return 참여자 프로필 목록
     */
    List<String> getParticipants(String roomId);
}
