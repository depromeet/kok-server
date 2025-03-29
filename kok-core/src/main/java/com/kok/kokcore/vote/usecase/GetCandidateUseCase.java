package com.kok.kokcore.vote.usecase;

import com.kok.kokcore.station.domain.entity.Station;
import com.kok.kokcore.vote.domain.Candidate;
import java.util.List;

public interface GetCandidateUseCase {

    List<Candidate> saveAndGetCandidates(String roomId, List<Station> stations);

    List<Candidate> getCandidates(String roomId);
}
