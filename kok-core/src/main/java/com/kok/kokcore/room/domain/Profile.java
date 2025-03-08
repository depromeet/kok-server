package com.kok.kokcore.room.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class Profile {
    private final String imageUrl;
    private final String nickname;

    public Profile(String imageUrl, String nickname) {
        this.imageUrl = imageUrl;
        this.nickname = nickname;
    }
}
