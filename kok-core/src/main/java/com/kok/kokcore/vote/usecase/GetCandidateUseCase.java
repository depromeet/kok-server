package com.kok.kokcore.vote.usecase;

import com.kok.kokcore.vote.domain.Candidate;
import java.util.List;

public interface GetCandidateUseCase {

    List<Candidate> getCandidate(String roomId);
}
