package com.kok.kokapi.room.application.service;

import com.kok.kokcore.location.domain.Location;
import com.kok.kokcore.location.port.out.ReadLocationPort;
import com.kok.kokcore.room.domain.Member;
import com.kok.kokcore.room.domain.Room;
import com.kok.kokcore.room.port.out.LoadRoomParticipantPort;
import com.kok.kokcore.room.port.out.LoadRoomPort;
import com.kok.kokcore.room.port.out.UpdateRoomPort;
import com.kok.kokcore.room.usecase.GetRoomUseCase;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomQueryService implements GetRoomUseCase {

    private final LoadRoomPort loadRoomPort;
    private final LoadRoomParticipantPort loadRoomParticipantPort;
    private final ReadLocationPort readLocationPort;
    private final UpdateRoomPort updateRoomPort;

    @Override
    public Room findRoomById(String roomId, LocalDateTime current) {
        Room room = getRoom(roomId);
        if (shouldEndLocationInput(room, current)) {
            room.startVote();
            updateRoomPort.update(room);
        }
        return getRoom(roomId);
    }

    private Room getRoom(String roomId) {
        return loadRoomPort.findRoomById(roomId)
            .orElseThrow(() -> new IllegalArgumentException("Room not found with id: " + roomId));
    }

    private boolean shouldEndLocationInput(Room room, LocalDateTime current) {
        List<Location> locations = readLocationPort.findLocationsByRoomId(room.getId());
        int locationInputCount = locations.size();
        return room.shouldEndLocationInput(locationInputCount, current);
    }

    @Override
    public List<Member> getParticipants(String roomId) {
        validate(roomId);
        return loadRoomParticipantPort.findMembersByRoomId(roomId);
    }

    private void validate(String roomId) {
        if (!loadRoomPort.isExistsByRoomId(roomId)) {
            throw new IllegalArgumentException("Room not found with id: " + roomId);
        }
    }

    @Override
    public Member getParticipant(String roomId, String memberId) {
        return getParticipants(roomId).stream()
            .filter(member -> member.getMemberId().equals(memberId))
            .findFirst()
            .orElseThrow(
                () -> new IllegalArgumentException("Member not found with id: " + memberId));
    }

    @Override
    public long getParticipantsCount(String roomId) {
        return readLocationPort.countParticipantsById(roomId);
    }
}
