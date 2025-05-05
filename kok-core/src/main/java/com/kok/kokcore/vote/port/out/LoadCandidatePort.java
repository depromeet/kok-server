package com.kok.kokcore.vote.port.out;

import com.kok.kokcore.vote.domain.Candidate;
import java.util.List;

public interface LoadCandidatePort {

    List<Candidate> findByRoomId(String roomId);

    boolean isExistsByRoomId(String roomId);
}
