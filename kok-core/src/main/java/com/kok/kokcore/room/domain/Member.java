package com.kok.kokcore.room.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class Member {
    private final String nickname;
    private final String profile;
    private final String role;

    public Member(String nickname, String profile, String role) {
        if (nickname == null || nickname.trim().isEmpty()) {
            throw new IllegalArgumentException("Nickname is required");
        }
        if (role == null || role.trim().isEmpty()) {
            throw new IllegalArgumentException("Role is required");
        }
        this.nickname = nickname.trim();
        this.profile = profile.trim();
        this.role = role.trim();
    }
}
