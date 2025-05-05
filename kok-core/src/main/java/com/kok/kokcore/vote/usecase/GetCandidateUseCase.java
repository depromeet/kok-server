package com.kok.kokcore.vote.usecase;

import com.kok.kokcore.station.domain.entity.Station;
import com.kok.kokcore.vote.domain.Candidate;
import java.util.List;
import java.util.Set;

public interface GetCandidateUseCase {

    List<Candidate> saveAndGetCandidates(String roomId, Set<Station> stations);
}
