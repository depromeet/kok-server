package com.kok.kokcore.room.usecase;

import com.kok.kokcore.room.domain.Member;
import com.kok.kokcore.room.domain.Room;

import java.util.List;

public interface GetRoomUseCase {
    Room findRoomById(String roomId);
    List<Member> getParticipants(String roomId);
}
