package com.kok.kokcore.vote;

import com.kok.kokcore.vote.domain.VoteResult;
import java.util.List;
import lombok.Getter;

@Getter
public class VoteResults {

    private final List<VoteResult> voteResults;

    public VoteResults(List<VoteResult> voteResults) {
        this.voteResults = voteResults;
    }

    public void applyResultTag() {
        int topVotedCount = voteResults.getFirst().getVotedCount();

        List<VoteResult> topResults = voteResults.stream()
            .filter(voteResult -> voteResult.getVotedCount() == topVotedCount)
            .toList();

        if (topResults.size() == 1) {
            topResults.getFirst().markTop();
            return;
        }

        for (VoteResult topResult : topResults) {
            topResult.markClose();
        }
    }

    public VoteResult getFinalResult() {
        return voteResults.getFirst();
    }
}
