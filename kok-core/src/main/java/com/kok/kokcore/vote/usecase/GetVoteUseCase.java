package com.kok.kokcore.vote.usecase;

import com.kok.kokcore.station.domain.entity.Station;
import com.kok.kokcore.vote.VoteResults;

public interface GetVoteUseCase {

    boolean isVotedByMember(String roomId, String memberId);

    Station getVoteFinalResult(String roomId);

    int countVotedMembers(String roomId);

    VoteResults getVoteResultsByRoomId(String roomId);
}
