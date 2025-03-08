package com.kok.kokapi.room.adapter.in.dto.response;

import com.kok.kokcore.room.domain.Member;
import com.kok.kokcore.room.domain.vo.MemberRole;

public record MemberResponse(
        String nickname,
        String profile,
        MemberRole role
) {
    public static MemberResponse from(Member member) {
        return new MemberResponse(member.getNickname(), member.getProfile(), member.getRole());
    }
}
