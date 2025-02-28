package com.kok.kokapi.room.adapter.in.dto.response;

import com.kok.kokcore.room.domain.Member;

public record MemberResponse(
        String nickname,
        String profile,
        String role
) {
    public static MemberResponse from(Member member) {
        return new MemberResponse(member.getNickname(), member.getProfile(), member.getRole());
    }
}
