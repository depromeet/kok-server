package com.kok.kokcore.vote.port.out;

import com.kok.kokcore.vote.domain.Vote;

public interface DeleteVotePort {

    void deleteByCandidate(Vote vote);

    void deleteAllByRoomIdAndMemberId(String roomId, String memberId);
}
