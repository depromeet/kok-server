package com.kok.kokcore.vote.domain;

import com.kok.kokcore.vote.domain.vo.VoteStatus;
import lombok.Getter;

@Getter
public class Vote {

    private Candidate candidate;
    private String memberId;
    private VoteStatus voteStatus;

    public Vote(Candidate candidate, String memberId, VoteStatus voteStatus) {
        this.candidate = candidate;
        this.memberId = memberId;
        this.voteStatus = voteStatus;
    }

    public Vote(String roomId, String memberId, long stationId) {
        this(new Candidate(roomId, stationId), memberId, VoteStatus.DISAGREE);
    }

    public void agree() {
        this.voteStatus = VoteStatus.AGREE;
    }

    public void disagree() {
        this.voteStatus = VoteStatus.DISAGREE;
    }

    public String getRoomId() {
        return candidate.getRoomId();
    }

    public long getStationId() {
        return candidate.getStationId();
    }
}
