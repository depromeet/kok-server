package com.kok.kokcore.vote.usecase;

import com.kok.kokcore.station.domain.entity.Station;

public interface GetVoteUseCase {

    boolean isVotedByMember(String roomId, String memberId);

    Station getVoteFinalResult(String roomId);
}
