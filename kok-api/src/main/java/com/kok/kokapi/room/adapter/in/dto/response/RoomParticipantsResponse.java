package com.kok.kokapi.room.adapter.in.dto.response;

import com.kok.kokcore.location.domain.Location;
import com.kok.kokcore.room.domain.Member;
import com.kok.kokcore.room.domain.Room;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record RoomParticipantsResponse(
    boolean isFull,
    List<RoomParticipantResponse> members
) {

    public static RoomParticipantsResponse of(Room room, List<Member> members, List<Location> locations) {
        Map<String, String> locationMap = locations.stream()
            .collect(Collectors.toMap(
                Location::getMemberId,
                Location::getName
            ));

        List<RoomParticipantResponse> responses = members.stream()
            .map(member -> new RoomParticipantResponse(
                member.getMemberId(),
                member.getProfile(),
                member.getNickname(),
                member.getRole(),
                locationMap.getOrDefault(member.getMemberId(), "")
            )).toList();

        return new RoomParticipantsResponse(room.isFull(members.size()), responses);
    }
}
