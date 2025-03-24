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

    public RoomDetailResponse findByRoomId(String roomId) {
        Room room = getRoomUseCase.findRoomById(roomId);
        int participantsCount = getRoomUseCase.getParticipantsCount(roomId);
        return RoomDetailResponse.of(room, participantsCount);
    }

    public RoomStatusResponse getRoomStatus(String roomId, LocalDateTime current) {
        Room room = getRoomUseCase.findRoomById(roomId);
        List<Location> locations = readLocationUseCase.readLocations(roomId);
        int locationInputCount = locations.size();
        boolean isVoteMode = room.hasLocationInputEnded(locationInputCount, current);
        return new RoomStatusResponse(isVoteMode);
    }
}
