package com.kok.kokcore.room.domain;

import com.kok.kokcore.room.domain.vo.MemberRole;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class Member {
    private final String memberId;
    private final String nickname;
    private final String profile;
    private final MemberRole role;

    public Member(String memberId, String nickname, String profile, MemberRole role) {
        if (memberId == null || memberId.isEmpty()) {
            throw new IllegalArgumentException("MemberId is required.");
        }
        if (nickname == null || nickname.isEmpty()) {
            throw new IllegalArgumentException("Nickname is required.");
        }
        if (profile == null || profile.isEmpty()) {
            throw new IllegalArgumentException("Profile is required.");
        }
        this.memberId = memberId;
        this.nickname = nickname.trim();
        this.profile = profile.trim();
        this.role = role;
    }
}
