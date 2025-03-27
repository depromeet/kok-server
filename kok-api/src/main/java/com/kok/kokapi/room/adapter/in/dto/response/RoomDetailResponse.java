package com.kok.kokapi.room.adapter.in.dto.response;

import com.kok.kokcore.room.domain.Room;

public record RoomDetailResponse(
    String id,
    String roomName,
    int nonParticipantCount
) {

    public static RoomDetailResponse of(Room room, int participantCount) {
        return new RoomDetailResponse(
            room.getId(),
            room.getRoomName(),
            room.getCapacity() - participantCount
        );
    }
}
