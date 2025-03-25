package com.kok.kokapi.vote.application.service;

import com.kok.kokcore.vote.application.port.out.SaveVotePort;
import com.kok.kokcore.vote.domain.Vote;
import com.kok.kokcore.vote.usecase.SaveVoteUseCase;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VoteService implements SaveVoteUseCase {

    private final SaveVotePort saveVotePort;

    @Override
    public void saveVotes(List<Vote> votes) {
        saveVotePort.saveAllByMember(votes);
        for (Vote vote : votes) {
            saveVotePort.saveByCandidate(vote);
        }
    }
}
