package com.kok.kokapi.room.adapter.in.dto.response;

import com.kok.kokcore.room.domain.Room;

public record RoomDetailResponse(
    String id,
    String roomName,
    int nonParticipantCount,
    boolean isFulled
) {

    public static RoomDetailResponse of(Room room, int participantCount) {
        return new RoomDetailResponse(
            room.getId(),
            room.getRoomName(),
            room.getCapacity() - participantCount,
            isFulled(room, participantCount)
        );
    }

    private static boolean isFulled(Room room, int participantCount) {
        return room.getCapacity() == participantCount;
    }
}
