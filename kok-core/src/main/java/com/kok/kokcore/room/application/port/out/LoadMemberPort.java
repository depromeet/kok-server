package com.kok.kokcore.room.application.port.out;

public interface LoadMemberPort {

    long countParticipantsByRoomId(String roomId);
}
