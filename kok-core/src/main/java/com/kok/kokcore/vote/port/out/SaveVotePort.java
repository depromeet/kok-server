package com.kok.kokcore.vote.port.out;

import java.util.List;

public interface SaveVotePort {

    void saveVotedStationsByRoomIdAndMemberId(List<Long> stationIds, String roomId,
        String memberId);

    void saveVotedMemberByRoomIdAndStationId(String memberId, String roomId, long stationId);

    void increaseVotedCountByRoomIdAndStationId(String roomId, long stationId);

    void saveVotedMemberByRoomId(String roomId, String memberId);
}
