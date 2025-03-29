package com.kok.kokcore.vote.usecase;

import com.kok.kokcore.vote.domain.Candidate;
import com.kok.kokcore.vote.domain.Vote;
import java.util.List;

public interface CreateVoteUseCase {

    void createVotes(List<Candidate> candidates, List<Vote> votes);
}
