package com.kok.kokapi.vote.application.service;

import com.kok.kokcore.room.domain.Room;
import com.kok.kokcore.room.port.out.LoadRoomPort;
import com.kok.kokcore.station.domain.entity.Station;
import com.kok.kokcore.vote.domain.Vote;
import com.kok.kokcore.vote.port.out.DeleteVotePort;
import com.kok.kokcore.vote.port.out.LoadVotePort;
import com.kok.kokcore.vote.port.out.SaveVotePort;
import com.kok.kokcore.vote.usecase.GetVoteUseCase;
import com.kok.kokcore.vote.usecase.SaveVoteUseCase;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VoteService implements SaveVoteUseCase, GetVoteUseCase {

    private final SaveVotePort saveVotePort;
    private final LoadVotePort loadVotePort;
    private final DeleteVotePort deleteVotePort;
    private final LoadRoomPort loadRoomPort;

    @Override
    public void saveVotes(List<Vote> votes) {
        String roomId = votes.getFirst().getRoomId();
        String memberId = votes.getFirst().getMemberId();
        initiate(roomId, memberId);
        saveVotePort.saveAllByMember(votes);
        for (Vote vote : votes) {
            saveVotePort.saveByCandidate(vote);
        }
    }

    private void initiate(String roomId, String memberId) {
        if (loadVotePort.isExistsByRoomIdAndMemberId(roomId, memberId)) {
            List<Vote> votes = loadVotePort.findAllByRoomIdAndMemberId(roomId, memberId);
            votes.forEach(deleteVotePort::deleteByCandidate);
            deleteVotePort.deleteAllByRoomIdAndMemberId(roomId, memberId);
        }
    }

    @Override
    public boolean isVotedByMember(String roomId, String memberId) {
        return loadVotePort.isExistsByRoomIdAndMemberId(roomId, memberId);
    }

    @Override
    public Station getVoteFinalResult(String roomId) {
        Room room = loadRoomPort.findRoomById(roomId)
            .orElseThrow(() -> new IllegalArgumentException("Cannot find room with id: " + roomId));
        validateRoomStatus(room);
        return null;
    }

    private static void validateRoomStatus(Room room) {
        if (!room.isVoteClosed()) {
            throw new IllegalArgumentException(
                "Vote is not closed for room with id: " + room.getId());
        }
    }
}
