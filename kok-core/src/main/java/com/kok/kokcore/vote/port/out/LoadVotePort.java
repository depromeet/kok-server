package com.kok.kokcore.vote.port.out;

import com.kok.kokcore.vote.domain.Vote;
import java.util.List;

public interface LoadVotePort {

    boolean isExistsByRoomIdAndMemberId(String roomId, String memberId);

    List<Vote> findAllByRoomIdAndMemberId(String roomId, String memberId);

    int countVotedMembersByRoomId(String roomId);

    List<String> findMemberIdsByRoomIdAndStationId(String roomId, long stationId);

    long findFirstStationIdByRoomIdOrderByVotedCount(String roomId);

    List<Long> findStationIdsByRoomIdOrderByVotedCount(String roomId);
}
