package com.kok.kokapi.room.adapter.out.persistence;

import com.kok.kokcore.application.port.out.LoadRoomPort;
import com.kok.kokcore.domain.Room;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RoomQueryRedisAdapter implements LoadRoomPort {

    @Override
    public Optional<Room> findRoomById(String roomId) {
        return Optional.empty();
    }
}
