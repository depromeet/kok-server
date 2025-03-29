package com.kok.kokapi.vote.application.service;

import com.kok.kokcore.station.domain.entity.Station;
import com.kok.kokcore.vote.application.port.out.LoadCandidatePort;
import com.kok.kokcore.vote.application.port.out.SaveCandidatePort;
import com.kok.kokcore.vote.domain.Candidate;
import com.kok.kokcore.vote.usecase.GetCandidateUseCase;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CandidateService implements GetCandidateUseCase {

    private final SaveCandidatePort saveCandidatePort;
    private final LoadCandidatePort loadCandidatePort;

    @Override
    public List<Candidate> getCandidate(String roomId, List<Station> stations) {
        if (!loadCandidatePort.isExistsByRoomId(roomId)) {
            List<Candidate> candidates = stations.stream()
                .map(station -> new Candidate(roomId, station.getId()))
                .toList();
            saveCandidatePort.saveAll(candidates);
        }
        return loadCandidatePort.findByRoomId(roomId);
    }
}
