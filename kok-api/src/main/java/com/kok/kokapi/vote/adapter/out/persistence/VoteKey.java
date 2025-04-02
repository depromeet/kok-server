package com.kok.kokapi.vote.adapter.out.persistence;

import com.kok.kokcore.vote.domain.Vote;
import com.kok.kokcore.vote.domain.vo.VoteStatus;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum VoteKey {

    VOTE_MEMBER_SET("vote:%s"),                             // Set<memberId>
    MEMBER_HASH("member:%s:%s"),                      // Hash<stationId, voteStatus>
    AGREE_COUNT_ZSET("agree:%s"),                      // ZSET<stationId, score>
    AGREE_MEMBER_SET("agree:%s:%d"),                    // Set<memberId> per station
    DISAGREE_COUNT_ZSET("disagree:%s"),                      // ZSET<stationId, score>
    DISAGREE_MEMBER_SET("disagree:%s:%d");              // Set<memberId> per station

    private final String format;

    // 투표 완료자 목록 key
    public static String voteKey(String roomId) {
        return String.format(VOTE_MEMBER_SET.format, roomId);
    }

    // 멤버별 투표 상세 저장 key
    public static String memberKey(String roomId, String memberId) {
        return String.format(MEMBER_HASH.format, roomId, memberId);
    }

    // 찬성/반대 ZSET key (stationId별 투표 수 저장)
    public static String voteStatusCountZSetKey(Vote vote) {
        return vote.getVoteStatus().isAgree()
            ? String.format(AGREE_COUNT_ZSET.format, vote.getRoomId())
            : String.format(DISAGREE_COUNT_ZSET.format, vote.getRoomId());
    }

    public static String voteStatusCountZSetKey(VoteStatus voteStatus, String roomId) {
        return voteStatus.isAgree()
            ? String.format(AGREE_COUNT_ZSET.format, roomId)
            : String.format(DISAGREE_COUNT_ZSET.format, roomId);
    }

    // 찬성/반대 Set key (stationId별 memberId 저장)
    public static String voteStatusMemberSetKey(Vote vote) {
        return vote.getVoteStatus().isAgree()
            ? String.format(AGREE_MEMBER_SET.format, vote.getRoomId(), vote.getStationId())
            : String.format(DISAGREE_MEMBER_SET.format, vote.getRoomId(), vote.getStationId());
    }

    public static String voteStatusMemberSetKey(
        VoteStatus voteStatus, String roomId, long stationId) {
        return voteStatus.isAgree()
            ? String.format(AGREE_MEMBER_SET.format, roomId, stationId)
            : String.format(DISAGREE_MEMBER_SET.format, roomId, stationId);
    }

    // 필드 이름으로 쓰일 stationId
    public static String stationField(long stationId) {
        return String.valueOf(stationId);
    }
}
