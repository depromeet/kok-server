package com.kok.kokapi.fixture;

import com.kok.kokcore.room.domain.Member;
import com.kok.kokcore.room.domain.Room;

public class RoomFixture {

    public static Room create(int capacity, Member member) {
        return Room.create("room", capacity, member);
    }
}
