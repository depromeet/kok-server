package com.kok.kokcore.vote.port.out;

public interface DeleteVotePort {

    /**
     * 투표한 멤버 Set에서 memberId 제거 votedMember:{roomId}:{stationId}
     */
    void deleteVotedMemberByRoomIdAndStationId(String memberId, String roomId, long stationId);

    /**
     * 후보지 투표 수 ZSet의 투표 수(score)를 -1 votedCount:{roomId}
     */
    void decreaseVotedCountByRoomIdAndStationId(String roomId, long stationId);

    /**
     * member의 개인 투표 기록 삭제 member:{roomId}:{memberId}
     */
    void deleteVotedStationsByRoomIdAndMemberId(String roomId, String memberId);

    /**
     * vote 완료 Set에서 memberId 제거 ex) vote:{roomId}
     */
    void deleteVotedMemberByRoomId(String roomId, String memberId);
}
