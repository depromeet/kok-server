package com.kok.kokcore.room.application.port.out;

import com.kok.kokcore.room.domain.Member;

public interface SaveRoomParticipantsPort {
    void joinRoom(String roomId, Member member);
}
