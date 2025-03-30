package com.kok.kokapi.vote.application.service;

import com.kok.kokcore.room.domain.Member;
import com.kok.kokcore.room.domain.Room;
import com.kok.kokcore.room.port.out.LoadRoomParticipantPort;
import com.kok.kokcore.room.port.out.LoadRoomPort;
import com.kok.kokcore.vote.domain.Candidate;
import com.kok.kokcore.vote.domain.Vote;
import com.kok.kokcore.vote.domain.vo.VoteStatus;
import com.kok.kokcore.vote.port.out.DeleteVotePort;
import com.kok.kokcore.vote.port.out.LoadCandidatePort;
import com.kok.kokcore.vote.port.out.LoadVotePort;
import com.kok.kokcore.vote.port.out.SaveVotePort;
import com.kok.kokcore.vote.usecase.GetVoteUseCase;
import com.kok.kokcore.vote.usecase.SaveVoteUseCase;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VoteService implements SaveVoteUseCase, GetVoteUseCase {

    private final SaveVotePort saveVotePort;
    private final LoadVotePort loadVotePort;
    private final LoadCandidatePort loadCandidatePort;
    private final DeleteVotePort deleteVotePort;
    private final LoadRoomPort loadRoomPort;
    private final LoadRoomParticipantPort loadRoomParticipantPort;

    @Override
    public void saveVotes(String roomId, String memberId, List<Long> agreedStationIds) {
        initiate(roomId, memberId);
        List<Vote> votes = getVotes(roomId, memberId, agreedStationIds);
        saveVotePort.saveAllByMember(votes);
        for (Vote vote : votes) {
            saveVotePort.saveByCandidate(vote);
        }
    }

    private List<Vote> getVotes(String roomId, String memberId, List<Long> stationIds) {
        List<Vote> votes = new ArrayList<>();
        List<Candidate> candidates = loadCandidatePort.findByRoomId(roomId);
        for (Candidate candidate : candidates) {
            if (isAgree(stationIds, candidate)) {
                votes.add(new Vote(candidate, memberId, VoteStatus.AGREE));
                continue;
            }
            votes.add(new Vote(candidate, memberId, VoteStatus.DISAGREE));
        }
        return votes;
    }

    private static boolean isAgree(List<Long> agreedStationIds, Candidate candidate) {
        return agreedStationIds.contains(candidate.getStationId());
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
    public int countVotedMembers(String roomId) {
        return loadVotePort.countMembersByRoomId(roomId);
    }

    @Override
    public List<Vote> getVotesByMember(String roomId, String memberId) {
        return List.of();
    }

    @Override
    public List<Member> getMembersByVote(Vote vote) {
        return List.of();
    }
}
