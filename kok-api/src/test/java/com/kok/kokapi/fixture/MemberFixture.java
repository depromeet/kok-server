package com.kok.kokapi.fixture;

import com.kok.kokcore.room.domain.Member;
import com.kok.kokcore.room.domain.vo.MemberRole;

public class MemberFixture {

    public static Member createFollower(){
        return new Member("follower", "profile.svg", MemberRole.FOLLOWER);
    }

    public static Member createLeader() {
        return new Member("leader", "profile.svg", MemberRole.LEADER);
    }
}
