package com.kok.kokapi.room.application.service;

import com.kok.kokcore.room.application.port.out.SaveRoomParticipantsPort;
import com.kok.kokcore.room.domain.Member;
import com.kok.kokcore.room.usecase.JoinRoomUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class RoomParticipantService implements JoinRoomUseCase {
    private final SaveRoomParticipantsPort saveRoomParticipantsPort;

    @Override
    public int joinRoom(String roomId, Member member) {
        return saveRoomParticipantsPort.joinRoom(roomId, member);
    }

}
