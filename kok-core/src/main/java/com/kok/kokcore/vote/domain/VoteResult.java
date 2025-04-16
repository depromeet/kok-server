package com.kok.kokcore.vote.domain;

import com.kok.kokcore.vote.domain.vo.ResultTag;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class VoteResult {

    private final Candidate candidate;
    private final List<String> memberIds;
    private ResultTag resultTag;

    public VoteResult(String roomId, long stationId, List<String> memberIds) {
        this(new Candidate(roomId, stationId), memberIds, ResultTag.NONE);
    }

    public VoteResult(
        Candidate candidate, List<String> memberIds, ResultTag resultTag) {
        this.candidate = candidate;
        this.memberIds = memberIds;
        this.resultTag = resultTag;
    }

    public int getVotedCount() {
        return memberIds.size();
    }

    public void markTop() {
        resultTag = ResultTag.TOP;
    }

    public void markClose() {
        resultTag = ResultTag.CLOSE;
    }

    public long getStationId() {
        return candidate.getStationId();
    }
}
