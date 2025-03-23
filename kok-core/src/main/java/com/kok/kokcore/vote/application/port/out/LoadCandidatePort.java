package com.kok.kokcore.vote.application.port.out;

import com.kok.kokcore.vote.domain.Candidate;
import java.util.List;

public interface LoadCandidatePort {

    List<Candidate> findByRoomId(String roomId);
}
