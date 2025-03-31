package com.kok.kokcore.vote.port.out;

import com.kok.kokcore.vote.domain.Vote;
import java.util.List;

public interface SaveVotePort {

    void saveByVoteStatus(Vote vote);

    void saveAllByMember(List<Vote> votes);
}
