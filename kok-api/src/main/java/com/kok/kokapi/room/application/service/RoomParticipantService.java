package com.kok.kokapi.room.application.service;

import com.kok.kokcore.room.application.port.out.LoadRoomParticipantsPort;
import com.kok.kokcore.room.application.port.out.SaveRoomParticipantsPort;
import com.kok.kokcore.room.domain.Member;
import com.kok.kokcore.room.domain.Profile;
import com.kok.kokcore.room.usecase.GetMemberProfileUseCase;
import com.kok.kokcore.room.usecase.JoinRoomUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class RoomParticipantService implements JoinRoomUseCase, GetMemberProfileUseCase {

    private final LoadRoomParticipantsPort loadRoomParticipantsPort;
    private final SaveRoomParticipantsPort saveRoomParticipantsPort;

    @Override
    public int joinRoom(String roomId, Member member) {
        return saveRoomParticipantsPort.joinRoom(roomId, member);
    }

    @Override
    public List<Profile> getProfilesByRoomId(String roomId) {
        return loadRoomParticipantsPort.getProfilesByRoomId(roomId);
    }
}
