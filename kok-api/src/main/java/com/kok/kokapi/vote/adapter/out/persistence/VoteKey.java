package com.kok.kokapi.vote.adapter.out.persistence;

import com.kok.kokcore.vote.domain.Vote;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum VoteKey {

    VOTE_COMPLETED_MEMBERS_SET("votedDoneMembers:%s"),                 // Set<memberId>
    VOTED_STATIONS_BY_MEMBER_SET("memberVoted:%s:%s"),                 // Set<StationId>
    VOTED_COUNT_OF_STATION_ZSET("votedCount:%s"),                      // ZSET<stationId, score>
    VOTED_MEMBERS_OF_STATION_SET("votedMembers:%s:%d")                 // Set<memberId> per station
    ;

    private final String format;

    // 투표 완료자 목록 key
    public static String voteCompletedMembersKey(String roomId) {
        return String.format(VOTE_COMPLETED_MEMBERS_SET.format, roomId);
    }

    // 멤버별 투표 상세 저장 key
    public static String votedStationsByMemberKey(String roomId, String memberId) {
        return String.format(VOTED_STATIONS_BY_MEMBER_SET.format, roomId, memberId);
    }

    // stationId별 투표 수 저장 key
    public static String votedCountOfStationKey(String roomId) {
        return String.format(VOTED_COUNT_OF_STATION_ZSET.format, roomId);
    }

    // stationId별 memberId 저장 key
    public static String votedMembersOfStationKey(Vote vote) {
        return String.format(VOTED_MEMBERS_OF_STATION_SET.format, vote.getRoomId(),
            vote.getStationId());
    }

    public static String votedMembersOfStationKey(String roomId, long stationId) {
        return String.format(VOTED_MEMBERS_OF_STATION_SET.format, roomId, stationId);
    }
}
