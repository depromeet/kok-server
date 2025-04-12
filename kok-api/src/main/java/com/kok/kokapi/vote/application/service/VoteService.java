package com.kok.kokapi.vote.application.service;

import com.kok.kokcore.room.domain.Member;
import com.kok.kokcore.room.domain.Room;
import com.kok.kokcore.room.port.out.LoadRoomParticipantPort;
import com.kok.kokcore.room.port.out.LoadRoomPort;
import com.kok.kokcore.station.domain.entity.Station;
import com.kok.kokcore.station.port.out.RetrieveStationsPort;
import com.kok.kokcore.vote.VoteResults;
import com.kok.kokcore.vote.domain.Vote;
import com.kok.kokcore.vote.domain.VoteResult;
import com.kok.kokcore.vote.port.out.DeleteVotePort;
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

    private static final int MINIMUM_VOTED_RATIO = 60;

    private final SaveVotePort saveVotePort;
    private final LoadVotePort loadVotePort;
    private final DeleteVotePort deleteVotePort;
    private final LoadRoomPort loadRoomPort;
    private final RetrieveStationsPort retrieveStationsPort;
    private final LoadRoomParticipantPort loadRoomParticipantPort;

    @Override
    public void saveVotes(String roomId, String memberId, List<Long> agreedStationIds) {
        validate(roomId, memberId);
        validateRoomStatusIfNotOnVote(roomId);
        initiate(roomId, memberId);

        // 1. 멤버가 투표한 stationId set 저장
        saveVotePort.saveVotedStationsByRoomIdAndMemberId(agreedStationIds, roomId, memberId);

        // 2. 각 후보에 대한 투표자 Set/ 투표 수 ZSet 갱신
        for (Long stationId : agreedStationIds) {
            saveVotePort.saveVotedMemberByRoomIdAndStationId(memberId, roomId, stationId);
            saveVotePort.increaseVotedCountByRoomIdAndStationId(roomId, stationId);
        }

        // 3. 투표 완료 Set에 멤버 추가
        saveVotePort.saveVotedMemberByRoomId(roomId, memberId);
    }

    private void initiate(String roomId, String memberId) {
        if (loadVotePort.isExistsByRoomIdAndMemberId(roomId, memberId)) {
            List<Vote> votes = loadVotePort.findAllByRoomIdAndMemberId(roomId, memberId);
            // 1. 각 후보에 대한 투표자 Set/ 투표 수 ZSet 갱신
            for (Vote vote : votes) {
                deleteVotePort.deleteVotedMemberByRoomIdAndStationId(
                    vote.getMemberId(), vote.getRoomId(), vote.getStationId());
                deleteVotePort.decreaseVotedCountByRoomIdAndStationId(
                    vote.getRoomId(), vote.getStationId());
            }

            // 2. 멤버가 투표한 stationId set 제거
            deleteVotePort.deleteVotedStationsByRoomIdAndMemberId(roomId, memberId);

            //3. 투표 완료 set에서 멤버 제거
            deleteVotePort.deleteVotedMemberByRoomId(roomId, memberId);
        }
    }

    @Override
    public boolean isVotedByMember(String roomId, String memberId) {
        return loadVotePort.isExistsByRoomIdAndMemberId(roomId, memberId);
    }

    @Override
    public int countVotedMembers(String roomId) {
        validate(roomId);
        validateRoomStatusIfNotOnVote(roomId);
        return loadVotePort.countVotedMembersByRoomId(roomId);
    }

    @Override
    public VoteResults getVoteResultsByRoomId(String roomId) {
        validate(roomId);
        validateRoomStatusIfNotOnVote(roomId);
        Room room = getRoom(roomId);
        List<Long> stationIds = loadVotePort.findStationIdsByRoomIdOrderByVotedCount(roomId);
        VoteResults voteResults = getVoteResults(room, stationIds);
        int votedCount = loadVotePort.countVotedMembersByRoomId(roomId);
        if (room.getVotedRatio(votedCount) > MINIMUM_VOTED_RATIO) {
            voteResults.applyResultTag();
        }
        return voteResults;
    }

    private VoteResults getVoteResults(Room room, List<Long> stationIds) {
        List<VoteResult> voteResults = new ArrayList<>();
        for (Long stationId : stationIds) {
            List<String> memberIds = loadVotePort.findMemberIdsByRoomIdAndStationId(
                room.getId(), stationId);
            Station station = getStation(stationId);
            voteResults.add(
                new VoteResult(room.getId(), stationId, memberIds, station.getPriority()));
        }
        return new VoteResults(voteResults);
    }

    @Override
    public Station getVoteFinalResult(String roomId) {
        validate(roomId);
        Room room = getRoom(roomId);
        validateRoomStatusIfVoteClosed(room);
        long stationId = loadVotePort.findFirstStationIdByRoomIdOrderByVotedCount(roomId);
        return getStation(stationId);
    }

    private void validate(String roomId, String memberId) {
        validate(roomId);
        List<String> memberIds = loadRoomParticipantPort.findMembersByRoomId(roomId).stream()
            .map(Member::getMemberId)
            .toList();
        if (!memberIds.contains(memberId)) {
            throw new IllegalArgumentException(
                String.format("Member not found with id: %s, in room with id: %s",
                    memberId, roomId));
        }
    }

    private void validate(String roomId) {
        if (!loadRoomPort.isExistsByRoomId(roomId)) {
            throw new IllegalArgumentException("Room not found with id: " + roomId);
        }
    }

    private void validateRoomStatusIfNotOnVote(String roomId) {
        Room room = getRoom(roomId);
        if (room.isNotOnVote()) {
            throw new IllegalStateException(
                "Room is not on vote status but status: " + room.getStatus());
        }
    }

    private Station getStation(long stationId) {
        return retrieveStationsPort.retrieveStation(stationId)
            .orElseThrow(
                () -> new IllegalArgumentException("Station not found with id " + stationId));
    }

    private Room getRoom(String roomId) {
        return loadRoomPort.findRoomById(roomId)
            .orElseThrow(() -> new IllegalArgumentException("Room not found with id: " + roomId));
    }

    private static void validateRoomStatusIfVoteClosed(Room room) {
        if (!room.isVoteClosed()) {
            throw new IllegalArgumentException(
                "Vote is not closed for room with id: " + room.getId());
        }
    }
}
