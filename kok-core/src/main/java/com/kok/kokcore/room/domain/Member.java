package com.kok.kokcore.room.domain;

import com.kok.kokcore.room.domain.vo.MemberRole;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class Member {
    private final String nickname;
    private final String profile;
    private final MemberRole role;

    public Member(String nickname, String profile, MemberRole role) {
        if (nickname == null || nickname.trim().isEmpty()) {
            throw new IllegalArgumentException("Nickname is required");
        }
        this.nickname = nickname.trim();
        this.profile = profile.trim();
        this.role = role;
    }
}
