package com.kok.kokapi.room.adapter.in.web;

import com.kok.kokapi.room.adapter.in.dto.CreateRoomRequest;
import com.kok.kokcore.domain.Room;
import com.kok.kokcore.usecase.CreateRoomUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final CreateRoomUseCase createRoomUseCase;

    @PostMapping("/create")
    public ResponseEntity<Room> createRoom(@RequestBody CreateRoomRequest request) {
        Room room = createRoomUseCase.createRoom(
                request.getRoomName(),
                request.getCapacity(),
                request.getHostProfile(),
                request.getPassword()
        );
        return ResponseEntity.ok(room);
    }
}
