package com.kok.kokcore.room.application.port.out;

import com.kok.kokcore.room.domain.Profile;

import java.util.List;

public interface LoadRoomParticipantsPort {
    List<Profile> getProfilesByRoomId(String roomId);
}
