package com.kok.kokapi.vote.adapter.in.dto.response;

import com.kok.kokcore.room.domain.Member;

public record VotedMemberResponse(
    String id,
    String imageUrl
) {

    public static VotedMemberResponse from(Member member) {
        return new VotedMemberResponse(member.getMemberId(), member.getProfile());
    }
}
