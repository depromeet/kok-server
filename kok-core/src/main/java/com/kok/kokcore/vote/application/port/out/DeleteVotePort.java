package com.kok.kokcore.vote.application.port.out;

import com.kok.kokcore.vote.domain.Vote;

public interface DeleteVotePort {

    void deleteByCandidate(Vote vote);
}
