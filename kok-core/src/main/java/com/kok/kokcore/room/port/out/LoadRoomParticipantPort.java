package com.kok.kokcore.room.port.out;

import com.kok.kokcore.room.domain.Member;
import java.util.List;

public interface LoadRoomParticipantPort {

    Long countParticipantsById(String roomId);

    List<Member> findMembersByRoomId(String roomId);
}
