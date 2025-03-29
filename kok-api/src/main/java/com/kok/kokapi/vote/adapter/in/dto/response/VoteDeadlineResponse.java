package com.kok.kokapi.vote.adapter.in.dto.response;

import com.kok.kokcore.room.domain.Room;
import java.time.LocalDateTime;

public record VoteDeadlineResponse(LocalDateTime endAt) {

    public static VoteDeadlineResponse from(Room room) {
        return new VoteDeadlineResponse(room.getVoteLimitDateTime());
    }
}
