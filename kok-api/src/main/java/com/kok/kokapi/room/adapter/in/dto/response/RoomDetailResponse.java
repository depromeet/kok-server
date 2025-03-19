package com.kok.kokapi.room.adapter.in.dto.response;

import com.kok.kokcore.room.domain.Room;

public record RoomDetailResponse(
        String id,
        String roomName,
        int capacity,
        int participantCount,
        int nonParticipantCount,
        boolean voteMode
) {
    public static RoomDetailResponse of(Room room, int participantCount, boolean isVoteMode) {
        return new RoomDetailResponse(
                room.getId(),
                room.getRoomName(),
                room.getCapacity(),
                participantCount,
                room.getCapacity() - participantCount,
                isVoteMode
        );
    }
}
