package com.kok.kokapi.vote.adapter.in.dto.response;

import com.kok.kokcore.room.domain.Member;
import com.kok.kokcore.station.domain.entity.Station;
import com.kok.kokcore.vote.domain.Vote;
import java.util.List;

public record ResultResponse(
    long stationId,
    String stationName,
    int votedCount,
    List<VotedMemberResponse> members
) {

    public static ResultResponse of(Station station, Vote vote, List<Member> members) {
        return new ResultResponse(
            station.getId(),
            station.getName(),
            members.size(),
            members.stream().map(VotedMemberResponse::from).toList()
        );
    }
}
