package com.kok.kokcore.vote.usecase;

public interface GetVoteUseCase {

    boolean isVotedByMember(String roomId, String memberId);
}
