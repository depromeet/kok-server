package com.kok.kokapi.room.adapter.in.dto.response;

import com.kok.kokcore.room.domain.Room;

public record RoomDetailResponse(
    String id,
    String roomName,
    long nonParticipantCount,
    String roomStatus
) {

    public static RoomDetailResponse of(Room room, long participantCount) {
        return new RoomDetailResponse(
            room.getId(),
            room.getRoomName(),
            room.getCapacity() - participantCount,
            room.getStatus().name()
        );
    }
}
