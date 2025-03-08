package com.kok.kokapi.room.adapter.in.dto.response;

import com.kok.kokcore.room.domain.Room;

public record RoomCreateResponse(
        String id,
        String roomName,
        int capacity,
        MemberResponse member,
        int memberId,
        int participantCount
) {
    public static RoomCreateResponse from(Room room, int memberId, int participantCount) {
        return new RoomCreateResponse(
                room.getId(),
                room.getRoomName(),
                room.getCapacity(),
                MemberResponse.from(room.getMember()),
                memberId,
                participantCount
        );
    }
}