package com.kok.kokapi.vote.adapter.in.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kok.kokcore.room.domain.Room;
import java.time.LocalDateTime;

public record VoteDeadlineResponse(
    int candidateCount,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    LocalDateTime endAt
) {

    public static VoteDeadlineResponse of(Room room, int candidateCount) {
        return new VoteDeadlineResponse(candidateCount, room.getVoteLimitDateTime());
    }
}
