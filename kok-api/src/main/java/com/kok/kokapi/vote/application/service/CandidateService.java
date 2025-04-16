package com.kok.kokapi.vote.application.service;

import com.kok.kokcore.station.domain.entity.Station;
import com.kok.kokcore.vote.domain.Candidate;
import com.kok.kokcore.vote.port.out.LoadCandidatePort;
import com.kok.kokcore.vote.port.out.SaveCandidatePort;
import com.kok.kokcore.vote.port.out.SaveVotePort;
import com.kok.kokcore.vote.usecase.GetCandidateUseCase;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CandidateService implements GetCandidateUseCase {

    private final SaveCandidatePort saveCandidatePort;
    private final LoadCandidatePort loadCandidatePort;
    private final SaveVotePort saveVotePort;

    @Override
    public List<Candidate> saveAndGetCandidates(String roomId, Set<Station> stations) {
        if (!loadCandidatePort.isExistsByRoomId(roomId)) {
            List<Candidate> candidates = stations.stream()
                .map(station -> new Candidate(roomId, station.getId()))
                .toList();
            saveCandidatePort.saveAll(candidates);
            initiateVoteScore(roomId, stations);
        }
        return loadCandidatePort.findByRoomId(roomId);
    }

    private void initiateVoteScore(String roomId, Set<Station> stations) {
        for (Station station : stations) {
            saveVotePort.initiateVoteScoreByRoomIdAndStationIdsAndStationPriority(
                roomId, station.getId(), station.getPriority()
            );
        }
    }
}
