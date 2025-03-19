package com.kok.kokapi.room.application.service;

import com.kok.kokapi.room.adapter.in.dto.response.RoomDetailResponse;
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

    public RoomDetailResponse findByRoomId(String roomId, LocalDateTime current){
        Room room = getRoomUseCase.findRoomById(roomId);
        List<Location> locations = readLocationUseCase.readLocations(roomId);
        int participantCount = locations.size();
        boolean isVoteMode = room.hasLocationInputEnded(participantCount, current);
        return RoomDetailResponse.of(room, participantCount, isVoteMode);
    }
}
