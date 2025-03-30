package com.kok.kokapi.vote.adapter.in.dto.response;

import java.util.List;

public record ResultResponse(
    long stationId,
    String stationName,
    String voteStatus,
    int votedCount,
    List<MemberResponse> members
) {

}
