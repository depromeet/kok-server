package com.kok.kokapi.vote.adapter.in.dto.response;

import com.kok.kokcore.room.domain.Member;
import com.kok.kokcore.station.domain.entity.Station;
import com.kok.kokcore.vote.domain.VoteResult;
import java.util.List;

public record ResultResponse(
    long stationId,
    String stationName,
    int votedCount,
    List<VotedMemberResponse> members,
    String resultTag
) {

    public static ResultResponse of(Station station, VoteResult voteResult, List<Member> members) {
        return new ResultResponse(
            station.getId(),
            station.getName(),
            voteResult.getVotedCount(),
            getVotedMemberResponses(members),
            voteResult.getResultTag().name()
        );
    }

    private static List<VotedMemberResponse> getVotedMemberResponses(List<Member> members) {
        return members.stream()
            .map(VotedMemberResponse::from)
            .toList();
    }
}
