package com.kok.kokcore.vote.domain.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum VoteStatus {

    AGREE("agree"),
    DISAGREE("disagree"),
    ;

    private final String name;

    public boolean isAgree() {
        return this.equals(AGREE);
    }

    public boolean isDisagree() {
        return this.equals(DISAGREE);
    }
}
