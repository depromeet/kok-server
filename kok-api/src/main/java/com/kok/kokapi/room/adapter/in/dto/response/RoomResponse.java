package com.kok.kokapi.room.adapter.in.dto.response;

import com.kok.kokcore.room.domain.Room;

public record RoomResponse(
        String id,
        String roomName,
        int capacity,
        String hostProfile,
        String roomLinkUrl
) {
    public static RoomResponse from(Room room) {
        return new RoomResponse(
                room.getId(),
                room.getRoomName(),
                room.getCapacity(),
                room.getHostProfile(),
                room.getRoomLinkUrl()
        );
    }
}
