package com.kok.kokcore.vote.port.out;

import com.kok.kokcore.vote.domain.Vote;
import java.util.List;

public interface SaveVotePort {

    void saveVoteMemberHash(List<Vote> votes);

    void saveVoteStatusSet(Vote vote);

    void incrementVoteStatusCountZSet(Vote vote);

    void saveVotedMemberSet(String roomId, String memberId);
}
