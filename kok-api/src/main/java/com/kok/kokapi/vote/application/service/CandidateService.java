package com.kok.kokapi.vote.application.service;

import com.kok.kokcore.station.domain.entity.Station;
import com.kok.kokcore.station.usecase.DeleteRecommendStationUseCase;
import com.kok.kokcore.station.usecase.GetRecommendStationUseCase;
import com.kok.kokcore.vote.domain.Candidate;
import com.kok.kokcore.vote.port.out.LoadCandidatePort;
import com.kok.kokcore.vote.port.out.SaveCandidatePort;
import com.kok.kokcore.vote.usecase.GetCandidateUseCase;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CandidateService implements GetCandidateUseCase {

    private final SaveCandidatePort saveCandidatePort;
    private final LoadCandidatePort loadCandidatePort;
    private final GetRecommendStationUseCase getRecommendStationUseCase;
    private final DeleteRecommendStationUseCase deleteRecommendStationUseCase;

    @Override
    public List<Candidate> getCandidate(String roomId) {
        if (!loadCandidatePort.isExistsByRoomId(roomId)) {
            List<Station> stations = getRecommendStationUseCase.getRecommendedStations(roomId);
            List<Candidate> candidates = stations.stream()
                .map(station -> new Candidate(roomId, station.getId()))
                .toList();
            saveCandidatePort.saveAll(candidates);
            deleteRecommendStationUseCase.deleteRecommendedStations(roomId);
        }
        return loadCandidatePort.findByRoomId(roomId);
    }
}
