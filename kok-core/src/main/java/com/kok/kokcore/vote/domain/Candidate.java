package com.kok.kokcore.vote.domain;


public class Candidate {

    private final String roomId;
    private final long stationId;

    public Candidate(String roomId, long stationId) {
        this.roomId = roomId;
        this.stationId = stationId;
    }
}
