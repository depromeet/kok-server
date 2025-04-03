package com.kok.kokapi.vote.adapter.in.dto.response;

import com.kok.kokcore.room.domain.Room;
import java.time.LocalDateTime;

public record VoteDeadlineResponse(int candidateCount, LocalDateTime endAt) {

    public static VoteDeadlineResponse of(Room room, int candidateCount) {
        return new VoteDeadlineResponse(candidateCount, room.getVoteLimitDateTime());
    }
}
