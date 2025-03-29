package com.kok.kokcore.vote.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Candidate {

    private final String roomId;
    private final long stationId;

    public Candidate(String roomId, long stationId) {
        this.roomId = roomId;
        this.stationId = stationId;
    }
}
