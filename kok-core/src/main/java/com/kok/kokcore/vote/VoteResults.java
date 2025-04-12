package com.kok.kokcore.vote;

import com.kok.kokcore.vote.domain.VoteResult;
import java.util.Comparator;
import java.util.List;
import lombok.Getter;

@Getter
public class VoteResults {

    private final List<VoteResult> voteResults;

    public VoteResults(List<VoteResult> voteResults) {
        this.voteResults = voteResults.stream()
            .sorted(Comparator
                .comparing(VoteResult::getVotedCount, Comparator.reverseOrder())
                .thenComparing(VoteResult::getPriority, Comparator.reverseOrder()))
            .toList();
    }
}
