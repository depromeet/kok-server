package com.kok.kokcore.vote.application.port.out;

import com.kok.kokcore.vote.domain.Vote;
import java.util.List;

public interface SaveVotePort {

    void saveAll(List<Vote> votes);

}
