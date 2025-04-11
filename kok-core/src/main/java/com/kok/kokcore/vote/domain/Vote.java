package com.kok.kokcore.vote.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Vote {

    private final Candidate candidate;
    private final String memberId;

    public Vote(Candidate candidate, String memberId) {
        this.candidate = candidate;
        this.memberId = memberId;
    }

    public Vote(String roomId, long stationId, String memberId) {
        this(new Candidate(roomId, stationId), memberId);
    }

    public String getRoomId() {
        return candidate.getRoomId();
    }

    public long getStationId() {
        return candidate.getStationId();
    }
}
