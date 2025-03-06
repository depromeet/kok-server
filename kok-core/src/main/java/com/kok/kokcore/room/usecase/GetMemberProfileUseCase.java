package com.kok.kokcore.room.usecase;

import com.kok.kokcore.room.domain.Profile;

import java.util.List;

public interface GetMemberProfileUseCase {
    List<Profile> getProfilesByRoomId(String roomId);
}
