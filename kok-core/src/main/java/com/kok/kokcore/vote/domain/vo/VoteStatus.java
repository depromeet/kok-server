package com.kok.kokcore.vote.domain.vo;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum VoteStatus {

    AGREE("agree"),
    DISAGREE("disagree"),
    ;

    private final String name;

    public static VoteStatus findByName(String name) {
        return Arrays.stream(values())
            .filter(voteStatus -> voteStatus.getName().equalsIgnoreCase(name))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("No status with name: " + name));
    }
}
