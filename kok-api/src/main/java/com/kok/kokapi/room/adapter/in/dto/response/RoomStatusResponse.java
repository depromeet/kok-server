package com.kok.kokapi.room.adapter.in.dto.response;

import com.kok.kokcore.room.domain.Room;

public record RoomStatusResponse(String roomStatus) {

    public static RoomStatusResponse of(Room room) {
        return new RoomStatusResponse(room.getStatus().name());
    }
}
