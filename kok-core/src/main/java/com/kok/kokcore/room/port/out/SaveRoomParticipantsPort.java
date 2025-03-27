package com.kok.kokcore.room.port.out;

import com.kok.kokcore.room.domain.Member;

public interface SaveRoomParticipantsPort {

    int joinRoom(String roomId, Member member);
}
