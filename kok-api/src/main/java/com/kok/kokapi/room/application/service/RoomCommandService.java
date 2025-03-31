package com.kok.kokapi.room.application.service;

import com.kok.kokcore.location.domain.Location;
import com.kok.kokcore.location.port.out.ReadLocationPort;
import com.kok.kokcore.room.domain.Member;
import com.kok.kokcore.room.domain.Room;
import com.kok.kokcore.room.port.out.LoadRoomPort;
import com.kok.kokcore.room.port.out.SaveRoomPort;
import com.kok.kokcore.room.port.out.UpdateRoomPort;
import com.kok.kokcore.room.usecase.CreateRoomUseCase;
import com.kok.kokcore.room.usecase.JoinRoomUseCase;
import com.kok.kokcore.room.usecase.UpdateRoomUseCase;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomCommandService implements CreateRoomUseCase, UpdateRoomUseCase {

    private final SaveRoomPort saveRoomPort;
    private final LoadRoomPort loadRoomPort;
    private final ReadLocationPort readLocationPort;
    private final UpdateRoomPort updateRoomPort;
    private final JoinRoomUseCase joinRoomUseCase;

    @Override
    public Room createRoom(String roomName, int capacity, Member host) {
        Room room = Room.create(roomName, capacity, host);
        Room savedRoom = saveRoomPort.save(room);

        joinRoomUseCase.joinRoom(savedRoom.getId(), savedRoom.getMember());
        return savedRoom;
    }

    @Override
    public void startVote(String roomId, LocalDateTime current) {
        Room room = getRoom(roomId);
        if (shouldUpdateVoteDeadline(room, current)) {
            room.updateVoteDeadline(current);
            room.startVote();
            updateRoomPort.update(room);
        }
    }

    @Override
    public void closeVote(String roomId) {
        Room room = getRoom(roomId);
        room.closeVote();
        updateRoomPort.update(room);
    }

    private Room getRoom(String roomId) {
        return loadRoomPort.findRoomById(roomId)
            .orElseThrow(
                () -> new IllegalArgumentException("Cannot find room with roomId: " + roomId));
    }

    private boolean shouldUpdateVoteDeadline(Room room, LocalDateTime current) {
        List<Location> locations = readLocationPort.findLocationsByRoomId(room.getId());
        int locationInputCount = locations.size();
        return room.shouldEndLocationInput(locationInputCount, current);
    }
}
