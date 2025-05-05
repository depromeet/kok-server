package com.kok.kokcore.room.port.out;

import com.kok.kokcore.room.domain.Member;
import java.util.List;
import java.util.Optional;

public interface LoadRoomParticipantPort {

    Long countParticipantsById(String roomId);

    List<Member> findMembersByRoomId(String roomId);

    Optional<Member> findByRoomIdAndMemberId(String roomId, String memberId);
}
