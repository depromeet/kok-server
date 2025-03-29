package com.kok.kokcore.vote.usecase;

import com.kok.kokcore.vote.domain.Vote;
import java.util.List;

public interface SaveVoteUseCase {

    void saveVotes(List<Vote> votes);
}
