package com.kok.kokcore.vote.port.out;

import com.kok.kokcore.vote.domain.Vote;

public interface DeleteVotePort {

    /**
     * 후보지 찬/반 Set에서 memberId 제거 {agree/disagree}:{roomId}:{stationId}
     */
    void removeMemberFromVoteStatusSet(Vote vote);

    /**
     * 후보지 찬/반 ZSet의 투표 수(score)를 -1 {agree/disagree}:{roomId}
     */
    void decrementVoteCountInZSet(Vote vote);

    /**
     * member의 개인 투표 기록 삭제 member:{roomId}:{memberId}
     */
    void deleteMemberVoteHash(String roomId, String memberId);

    /**
     * vote 완료 Set에서 memberId 제거 ex) vote:{roomId}
     */
    void removeMemberFromVotedSet(String roomId, String memberId);
}
