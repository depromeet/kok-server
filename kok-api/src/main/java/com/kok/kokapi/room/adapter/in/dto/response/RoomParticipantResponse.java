package com.kok.kokapi.room.adapter.in.dto.response;

import com.kok.kokcore.room.domain.vo.MemberRole;

public record RoomParticipantResponse(
    String memberId,
    String profile,
    String nickname,
    MemberRole role,
    String address
) { }
