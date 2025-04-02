package com.kok.kokcore.vote.port.out;

import com.kok.kokcore.vote.domain.Vote;
import com.kok.kokcore.vote.domain.vo.VoteStatus;
import java.util.List;

public interface LoadVotePort {

    boolean isExistsByRoomIdAndMemberId(String roomId, String memberId);

    List<Vote> findAllByRoomIdAndMemberId(String roomId, String memberId);

    int countMembersByRoomId(String roomId);

    List<String> findMemberIdsByRoomIdAndStationIdAndStatus(String roomId, long stationId,
        VoteStatus voteStatus);

    long getFirstStationIdByRoomIdAndVoteStatus(String roomId, VoteStatus voteStatus);
}
