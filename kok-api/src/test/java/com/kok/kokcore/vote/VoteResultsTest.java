package com.kok.kokcore.vote;

import static org.assertj.core.api.Assertions.assertThat;

import com.kok.kokcore.vote.domain.VoteResult;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class VoteResultsTest {

    @DisplayName("주어진 투표 결과를 투표 수가 같다면, 가중치 순으로 정렬한다.")
    @Test
    void orderByPriorityIfVoteCountSame() {
        VoteResult voteResult = new VoteResult("roomId", 1, List.of("1"), 3);
        VoteResult voteResult2 = new VoteResult("roomId", 1, List.of("1"), 4);
        VoteResults voteResults = new VoteResults(List.of(voteResult, voteResult2));

        assertThat(voteResults.getVoteResults()).containsExactly(voteResult2, voteResult);
    }

}
