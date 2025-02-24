package com.kok.kokapi.room.adapter.in.dto.response;

import com.kok.kokcore.room.domain.Room;

public record RoomDetailResponse(
        String id,
        String roomName,
        int capacity,
        String hostProfile,
        String roomLinkUrl
) {
    public static RoomDetailResponse from(Room room) {
        return new RoomDetailResponse(
                room.getId(),
                room.getRoomName(),
                room.getCapacity(),
                room.getHostProfile(),
                room.getRoomLinkUrl()
        );
    }
}
