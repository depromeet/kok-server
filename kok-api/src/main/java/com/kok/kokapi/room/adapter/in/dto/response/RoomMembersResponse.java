package com.kok.kokapi.room.adapter.in.dto.response;

import com.kok.kokcore.room.domain.vo.MemberRole;

public record RoomMembersResponse(
    String memberId,
    String profile,
    String nickname,
    MemberRole role
) {

}
