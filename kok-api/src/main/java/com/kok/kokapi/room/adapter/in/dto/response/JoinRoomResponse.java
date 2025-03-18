package com.kok.kokapi.room.adapter.in.dto.response;

public record JoinRoomResponse(
        String id,
        String profile,
        String nickname,
        int participantCount,
        int nonParticipantCount
) { }

