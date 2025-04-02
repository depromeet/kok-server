package com.kok.kokapi.vote.adapter.in.dto.response;

import com.kok.kokcore.location.domain.Location;
import com.kok.kokcore.room.domain.Member;

public record MemberVoteStatusResponse(
    String memberId,
    String nickname,
    String imageUrl,
    String address,
    boolean isVoted
) {

    public static MemberVoteStatusResponse of(Member member, Location location, boolean isVoted) {
        return new MemberVoteStatusResponse(
            member.getMemberId(),
            member.getNickname(),
            member.getProfile(),
            location.getName(),
            isVoted
        );
    }
}
