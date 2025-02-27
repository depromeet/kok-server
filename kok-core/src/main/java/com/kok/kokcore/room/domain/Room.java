package com.kok.kokcore.room.domain;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode
public class Room implements Serializable {

    public static final int REQUIRED_CAPACITY = 2;
    public static final String ROOM_LINK_URL = "kakao://app/room?roomId=";

    private final String id;               // 약속방 ID (UUID)
    private final String roomName;         // 약속방 이름
    private final int capacity;            // 참여인원 수 (최소 2명 이상)
    private final String password;         // 방 비밀번호 (옵션)
    private final Member member;             // 방 참여자
    private final String roomLinkUrl;      // 생성된 약속방 입장 링크

    private Room(String id, String roomName, int capacity, Member member,
                String password, String roomLinkUrl) {
        this.id = id;
        this.roomName = roomName;
        this.capacity = capacity;
        this.member = member;
        this.password = password;
        this.roomLinkUrl = roomLinkUrl;
    }

    public static Room create(String roomName, int capacity, Member host, String password) {
        validateParameter(roomName, capacity, host);

        String roomId = UUID.randomUUID().toString();
        String roomLinkUrl = ROOM_LINK_URL + roomId;

        return new Room(roomId, roomName, capacity, host, password, roomLinkUrl);
    }

    private static void validateParameter(String roomName, int capacity, Member host) {
        if (roomName == null || roomName.trim().isEmpty()) {
            throw new IllegalArgumentException("Room name is required");
        }
        if (capacity < REQUIRED_CAPACITY) {
            throw new IllegalArgumentException("At least 2 participants are required");
        }
    }
}
