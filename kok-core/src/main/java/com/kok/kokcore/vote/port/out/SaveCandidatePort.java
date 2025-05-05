package com.kok.kokcore.vote.port.out;

import com.kok.kokcore.vote.domain.Candidate;
import java.util.List;

public interface SaveCandidatePort {

    void saveAll(List<Candidate> candidates);
}
