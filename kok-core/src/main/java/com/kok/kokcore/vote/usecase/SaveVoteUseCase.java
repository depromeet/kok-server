package com.kok.kokcore.vote.usecase;

import java.util.List;

public interface SaveVoteUseCase {

    void saveVotes(String roomId, String memberId, List<Long> agreedStationIds);
}
