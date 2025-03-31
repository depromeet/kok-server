package com.kok.kokapi.vote.adapter.in.dto.response;

import java.util.List;

public record VoteResultResponse(
    int notVotedCount,
    List<ResultResponse> results
) {

}
