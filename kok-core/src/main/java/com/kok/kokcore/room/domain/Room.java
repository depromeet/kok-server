package com.kok.kokcore.room.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class Room implements Serializable {

    private static final int REQUIRED_CAPACITY = 2;
    private static final long LOCATION_INPUT_TIME_LIMIT = 6;

    private final String id;               // 약속방 ID (UUID)
    private final String roomName;         // 약속방 이름
    private final int capacity;            // 참여인원 수 (최소 2명 이상)
    private final Member member;           // 방 참여자
    private final LocalDateTime locationInputLimitDateTime; // 출발지 입력 마감일시
    private final LocalDateTime createdDateTime; // 방 생성일시

    private Room(String id, String roomName, int capacity, Member member) {
        this.id = id;
        this.roomName = roomName;
        this.capacity = capacity;
        this.member = member;
        this.createdDateTime = LocalDateTime.now().withNano(0);
        this.locationInputLimitDateTime = createdDateTime.plusHours(LOCATION_INPUT_TIME_LIMIT);
    }

    public static Room create(String roomName, int capacity, Member member) {
        validateParameter(roomName, capacity);

        String roomId = UUID.randomUUID().toString();

        return new Room(roomId, roomName, capacity, member);
    }

    private static void validateParameter(String roomName, int capacity) {
        if (roomName == null || roomName.trim().isEmpty()) {
            throw new IllegalArgumentException("Room name is required");
        }
        if (capacity < REQUIRED_CAPACITY) {
            throw new IllegalArgumentException("At least 2 participants are required");
        }
    }

    public boolean hasLocationInputEnded(long locationInputCount, LocalDateTime currentTime) {
        return isAllLocationInput(locationInputCount) || currentTime.isAfter(
            locationInputLimitDateTime);
    }

    private boolean isAllLocationInput(long participantCount) {
        return participantCount == capacity;
    }

    public boolean isFull(int participantCount) {
        return capacity == participantCount;
    }
}
