package com.kok.kokapi.room.adapter.in.dto.response;

import com.kok.kokcore.room.domain.Member;
import com.kok.kokcore.room.domain.Room;
import java.util.List;

public record RoomMembersResponses(
    boolean isFull,
    List<RoomMembersResponse> members
) {

    public static RoomMembersResponses of(Room room, List<Member> members) {
        return new RoomMembersResponses(room.isFull(members.size()), getMemberResponses(members));
    }

    private static List<RoomMembersResponse> getMemberResponses(List<Member> members) {
        return members.stream()
            .map(member -> new RoomMembersResponse(
                member.getMemberId(),
                member.getProfile(),
                member.getNickname(),
                member.getRole()
            )).toList();
    }
}
