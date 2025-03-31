package com.kok.kokcore.vote.usecase;

import com.kok.kokcore.room.domain.Member;
import com.kok.kokcore.station.domain.entity.Station;
import com.kok.kokcore.vote.domain.Vote;
import java.util.List;

public interface GetVoteUseCase {

    boolean isVotedByMember(String roomId, String memberId);

    Station getVoteFinalResult(String roomId);

    int countVotedMembers(String roomId);

    List<Vote> getVotesByMember(String roomId, String memberId);

    List<Member> getMembersByVote(Vote vote);
}
