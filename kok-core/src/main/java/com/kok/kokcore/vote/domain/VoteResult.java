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
    private final long priority;
    private ResultTag resultTag;

    public VoteResult(String roomId, long stationId, List<String> memberIds, long priority) {
        this(new Candidate(roomId, stationId), memberIds, priority, ResultTag.NONE);
    }

    public VoteResult(
        Candidate candidate, List<String> memberIds, long priority, ResultTag resultTag) {
        this.candidate = candidate;
        this.memberIds = memberIds;
        this.priority = priority;
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
}
