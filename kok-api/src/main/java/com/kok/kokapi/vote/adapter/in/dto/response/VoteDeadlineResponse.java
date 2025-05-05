package com.kok.kokapi.vote.adapter.in.dto.response;

import com.kok.kokcore.room.domain.Room;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

public record VoteDeadlineResponse(
    int candidateCount,
    LocalDateTime endAt
) {

    public static VoteDeadlineResponse of(Room room, int candidateCount) {
        return new VoteDeadlineResponse(
            candidateCount,
            room.getVoteLimitDateTime()
                .atZone(ZoneOffset.UTC)
                .withZoneSameInstant(ZoneId.of("Asia/Seoul"))
                .toLocalDateTime()
        );
    }
}
