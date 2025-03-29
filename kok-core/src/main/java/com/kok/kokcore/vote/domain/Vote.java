package com.kok.kokcore.vote.domain;

import com.kok.kokcore.vote.domain.vo.VoteStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Vote {

    private final Candidate candidate;
    private final String memberId;
    private final VoteStatus voteStatus;

    public Vote(Candidate candidate, String memberId, VoteStatus voteStatus) {
        this.candidate = candidate;
        this.memberId = memberId;
        this.voteStatus = voteStatus;
    }

    public Vote(String roomId, String memberId, long stationId) {
        this(new Candidate(roomId, stationId), memberId, VoteStatus.DISAGREE);
    }

    public Vote(String roomId, Long stationId, String memberId, String voteStatus) {
        this(new Candidate(roomId, stationId), memberId, VoteStatus.findByName(voteStatus));
    }

    public String getRoomId() {
        return candidate.getRoomId();
    }

    public long getStationId() {
        return candidate.getStationId();
    }
}
