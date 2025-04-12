package com.kok.kokcore.vote;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.kok.kokcore.vote.domain.VoteResult;
import com.kok.kokcore.vote.domain.vo.ResultTag;
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

    @DisplayName("최고 득표 후보지가 1개인 경우 TOP 태그가 부여된다.")
    @Test
    void applyResultTagWithSingleTopVoted() {
        // given
        VoteResult voteResult = new VoteResult("roomId", 1, List.of("a", "b", "c"), 1);
        VoteResult voteResult2 = new VoteResult("roomId", 2, List.of("a", "b"), 2);
        VoteResult voteResult3 = new VoteResult("roomId", 3, List.of("a"), 3);
        VoteResults voteResults = new VoteResults(List.of(voteResult, voteResult2, voteResult3));

        // when
        voteResults.applyResultTag();

        // then
        assertAll(
            () -> assertThat(voteResults.getVoteResults().get(0).getVotedCount())
                .isEqualTo(3),
            () -> assertThat(voteResults.getVoteResults().get(0).getResultTag())
                .isEqualTo(ResultTag.TOP),
            () -> assertThat(voteResults.getVoteResults().get(1).getVotedCount())
                .isEqualTo(2),
            () -> assertThat(voteResults.getVoteResults().get(1).getResultTag())
                .isEqualTo(ResultTag.NONE),
            () -> assertThat(voteResults.getVoteResults().get(2).getVotedCount())
                .isEqualTo(1),
            () -> assertThat(voteResults.getVoteResults().get(2).getResultTag())
                .isEqualTo(ResultTag.NONE)
        );
    }

    @DisplayName("최고 득표 후보지가 여러 개이면 모두에게 CLOSE 태그가 부여된다.")
    @Test
    void applyResultTagWithTiedTopVotes() {
        // given
        VoteResult close = new VoteResult("roomId", 1, List.of("a", "b"), 1);
        VoteResult close2 = new VoteResult("roomId", 2, List.of("a", "b"), 2);
        VoteResult low = new VoteResult("roomId", 3, List.of("a"), 3);
        VoteResults voteResults = new VoteResults(List.of(close, close2, low));

        // when
        voteResults.applyResultTag();

        // then
        assertAll(
            () -> assertThat(voteResults.getVoteResults().get(0).getVotedCount())
                .isEqualTo(2),
            () -> assertThat(voteResults.getVoteResults().get(0).getResultTag())
                .isEqualTo(ResultTag.CLOSE),
            () -> assertThat(voteResults.getVoteResults().get(1).getVotedCount())
                .isEqualTo(2),
            () -> assertThat(voteResults.getVoteResults().get(1).getResultTag())
                .isEqualTo(ResultTag.CLOSE),
            () -> assertThat(voteResults.getVoteResults().get(2).getVotedCount())
                .isEqualTo(1),
            () -> assertThat(voteResults.getVoteResults().get(2).getResultTag())
                .isEqualTo(ResultTag.NONE)
        );
    }
}
