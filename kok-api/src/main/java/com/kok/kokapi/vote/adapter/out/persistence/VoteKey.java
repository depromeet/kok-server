package com.kok.kokapi.vote.adapter.out.persistence;

import com.kok.kokcore.vote.domain.Vote;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum VoteKey {

    VOTE_MEMBER_SET("vote:%s"),                             // Set<memberId>
    MEMBER_SET("member:%s:%s"),                      // Set<StationId>
    VOTED_COUNT_ZSET("votedCount:%s"),                      // ZSET<stationId, score>
    VOTED_MEMBER_SET("votedMember:%s:%d")                    // Set<memberId> per station
    ;

    private final String format;

    // 투표 완료자 목록 key
    public static String voteKey(String roomId) {
        return String.format(VOTE_MEMBER_SET.format, roomId);
    }

    // 멤버별 투표 상세 저장 key
    public static String memberKey(String roomId, String memberId) {
        return String.format(MEMBER_SET.format, roomId, memberId);
    }

    // stationId별 투표 수 저장 key
    public static String votedCountOfStationIdKey(Vote vote) {
        return String.format(VOTED_COUNT_ZSET.format, vote.getRoomId());
    }

    public static String votedCountOfStationIdKey(String roomId) {
        return String.format(VOTED_COUNT_ZSET.format, roomId);
    }

    // stationId별 memberId 저장 key
    public static String votedMembersOfStationKey(Vote vote) {
        return String.format(VOTED_MEMBER_SET.format, vote.getRoomId(), vote.getStationId());
    }

    public static String votedMembersOfStationKey(String roomId, long stationId) {
        return String.format(VOTED_MEMBER_SET.format, roomId, stationId);
    }
}
