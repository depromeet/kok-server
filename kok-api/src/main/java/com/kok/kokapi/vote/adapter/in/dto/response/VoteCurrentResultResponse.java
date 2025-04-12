package com.kok.kokapi.vote.adapter.in.dto.response;

import java.util.List;

public record VoteCurrentResultResponse(
    int notVotedCount,
    List<ResultResponse> results
) {

}
