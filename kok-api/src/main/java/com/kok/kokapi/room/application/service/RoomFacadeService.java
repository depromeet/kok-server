package com.kok.kokapi.room.application.service;

import com.kok.kokapi.room.adapter.in.dto.response.RoomDetailResponse;
import com.kok.kokapi.room.adapter.in.dto.response.RoomStatusResponse;
import com.kok.kokcore.location.domain.Location;
import com.kok.kokcore.location.usecase.ReadLocationUseCase;
import com.kok.kokcore.room.domain.Room;
import com.kok.kokcore.room.usecase.GetRoomUseCase;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomFacadeService {

    private final GetRoomUseCase getRoomUseCase;
    private final ReadLocationUseCase readLocationUseCase;

    public RoomDetailResponse findByRoomId(String roomId, LocalDateTime current) {
        Room room = getRoomUseCase.findRoomById(roomId);
        int participantsCount = getRoomUseCase.getParticipantsCount(roomId);
        boolean isVoteMode = getVoteMode(roomId, current, room);
        return RoomDetailResponse.of(room, participantsCount, isVoteMode);
    }

    public RoomStatusResponse getRoomStatus(String roomId, LocalDateTime current) {
        Room room = getRoomUseCase.findRoomById(roomId);
        boolean isVoteMode = getVoteMode(roomId, current, room);
        return new RoomStatusResponse(isVoteMode);
    }

    private boolean getVoteMode(String roomId, LocalDateTime current, Room room) {
        List<Location> locations = readLocationUseCase.readLocations(roomId);
        int locationInputCount = locations.size();
        return room.hasLocationInputEnded(locationInputCount, current);
    }
}
