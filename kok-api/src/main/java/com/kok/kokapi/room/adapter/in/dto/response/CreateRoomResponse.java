package com.kok.kokapi.room.adapter.in.dto.response;

import com.kok.kokcore.room.domain.Room;

public record CreateRoomResponse(
    String id,
    String roomName,
    int capacity,
    MemberResponse member,
    int participantCount,
    int nonParticipantCount
) {

    public static CreateRoomResponse of(Room room, int participantCount, int nonParticipantCount) {
        return new CreateRoomResponse(
            room.getId(),
            room.getRoomName(),
            room.getCapacity(),
            MemberResponse.from(room.getMember()),
            participantCount,
            nonParticipantCount
        );
    }
}
