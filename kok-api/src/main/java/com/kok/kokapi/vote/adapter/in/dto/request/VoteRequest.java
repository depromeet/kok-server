package com.kok.kokapi.vote.adapter.in.dto.request;

import java.util.List;

public record VoteRequest(
    List<Long> agreedStationIds
) {

}
