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
    private final String hostProfile;      // 방장 프로필 정보
    private final String password;         // 방 비밀번호 (옵션)
    private final String roomLinkUrl;      // 생성된 약속방 입장 링크

    public Room(String id, String roomName, int capacity, String hostProfile,
                String password, String roomLinkUrl) {
        this.id = id;
        this.roomName = roomName;
        this.capacity = capacity;
        this.hostProfile = hostProfile;
        this.password = password;
        this.roomLinkUrl = roomLinkUrl;
    }

    public static Room create(String roomName, int capacity, String hostProfile, String password) {
        if (roomName == null || roomName.trim().isEmpty()) {
            throw new IllegalArgumentException("Room name is required");
        }
        if (capacity < REQUIRED_CAPACITY) {
            throw new IllegalArgumentException("At least 2 participants are required");
        }
        if (hostProfile == null || hostProfile.trim().isEmpty()) {
            throw new IllegalArgumentException("Host profile is required");
        }

        String roomId = UUID.randomUUID().toString();
        String roomLinkUrl = ROOM_LINK_URL + roomId;

        return new Room(roomId, roomName, capacity, hostProfile, password, roomLinkUrl);
    }
}
