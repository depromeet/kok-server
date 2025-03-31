package com.kok.kokapi.vote.application.service;

import com.kok.kokcore.room.domain.Member;
import com.kok.kokcore.room.domain.Room;
import com.kok.kokcore.room.port.out.LoadRoomParticipantPort;
import com.kok.kokcore.room.port.out.LoadRoomPort;
import com.kok.kokcore.station.domain.entity.Station;
import com.kok.kokcore.station.port.out.RetrieveStationsPort;
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
import java.util.Optional;
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
    private final RetrieveStationsPort retrieveStationsPort;
    private final LoadRoomParticipantPort loadRoomParticipantPort;

    @Override
    public void saveVotes(String roomId, String memberId, List<Long> agreedStationIds) {
        validate(roomId, memberId);
        initiate(roomId, memberId);
        List<Vote> votes = getVotes(roomId, memberId, agreedStationIds);
        saveVotePort.saveAllByMember(votes);
        for (Vote vote : votes) {
            saveVotePort.saveByVoteStatus(vote);
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
        validate(roomId);
        return loadVotePort.countMembersByRoomId(roomId);
    }

    @Override
    public List<Vote> getVotesByMember(String roomId, String memberId) {
        validate(roomId, memberId);
        validateVote(roomId, memberId);
        return loadVotePort.findAllByRoomIdAndMemberId(roomId, memberId);
    }

    @Override
    public List<Member> getMembersByVote(Vote vote) {
        List<String> memberIds = loadVotePort.findMemberIdsByRoomIdAndStationIdAndStatus(
            vote.getRoomId(), vote.getStationId(), vote.getVoteStatus());
        return getMembers(memberIds, vote.getRoomId());
    }

    private List<Member> getMembers(List<String> memberIds, String roomId) {
        return memberIds.stream()
            .map(memberId -> loadRoomParticipantPort.findByRoomIdAndMemberId(roomId, memberId))
            .flatMap(Optional::stream)
            .toList();
    }

    private void validate(String roomId, String memberId) {
        validate(roomId);
        List<String> memberIds = loadRoomParticipantPort.findMembersByRoomId(roomId).stream()
            .map(Member::getMemberId)
            .toList();
        if (!memberIds.contains(memberId)) {
            throw new IllegalArgumentException(
                String.format("Cannot find member with id: %s, in room with id: %s", memberId,
                    roomId));
        }
    }

    private void validate(String roomId) {
        if (!loadRoomPort.isExistsByRoomId(roomId)) {
            throw new IllegalArgumentException("Cannot find room with roomId: " + roomId);
        }
        Room room = getRoom(roomId);
        if (room.isNotOnVote()) {
            throw new IllegalStateException(
                "Room is not on vote status but status: " + room.getStatus());
        }
    }

    private Room getRoom(String roomId) {
        return loadRoomPort.findRoomById(roomId)
            .orElseThrow(() -> new IllegalArgumentException("Room not found with id: " + roomId));
    }

    private void validateVote(String roomId, String memberId) {
        if (!loadVotePort.isExistsByRoomIdAndMemberId(roomId, memberId)) {
            throw new IllegalArgumentException(
                String.format("Not voted by member with id: %s, in room with id: %s", memberId,
                    roomId));
        }
    }

    @Override
    public Station getVoteFinalResult(String roomId) {
        Room room = loadRoomPort.findRoomById(roomId)
            .orElseThrow(() -> new IllegalArgumentException("Cannot find room with id: " + roomId));
        validateRoomStatus(room);
        long stationId = loadVotePort.getFirstStationIdByRoomIdAndVoteStatus(
            roomId, VoteStatus.AGREE);
        Station station = retrieveStationsPort.retrieveStation(stationId)
            .orElseThrow(
                () -> new IllegalArgumentException("Cannot find station with id " + stationId));
        return station;
    }

    private static void validateRoomStatus(Room room) {
        if (!room.isVoteClosed()) {
            throw new IllegalArgumentException(
                "Vote is not closed for room with id: " + room.getId());
        }
    }
}
