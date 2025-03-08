package com.kok.kokapi.room.adapter.in.dto.response;

import com.kok.kokcore.room.domain.Room;

public record RoomDetailResponse(
        String id,
        String roomName,
        int capacity,
        MemberResponse member
) {
    public static RoomDetailResponse from(Room room) {
        return new RoomDetailResponse(
                room.getId(),
                room.getRoomName(),
                room.getCapacity(),
                getLeaderResponse(room)
        );
    }

    private static MemberResponse getLeaderResponse(Room room) {
        return MemberResponse.from(room.getMember());
    }
}
