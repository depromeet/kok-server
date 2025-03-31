package com.kok.kokapi.room.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.kok.kokapi.centroid.adapter.out.persistence.LocationRepository;
import com.kok.kokapi.common.template.ServiceTest;
import com.kok.kokapi.fixture.MemberFixture;
import com.kok.kokapi.fixture.PointFixture;
import com.kok.kokapi.room.adapter.out.persistence.RoomParticipantSaveAdapter;
import com.kok.kokapi.room.adapter.out.persistence.RoomQueryRedisAdapter;
import com.kok.kokapi.room.adapter.out.persistence.RoomSaveRedisAdapter;
import com.kok.kokcore.location.domain.Location;
import com.kok.kokcore.room.domain.Member;
import com.kok.kokcore.room.domain.Room;
import com.kok.kokcore.room.domain.vo.MemberRole;
import com.kok.kokcore.room.domain.vo.RoomStatus;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class RoomCommandServiceTest extends ServiceTest {

    @Autowired
    private RoomCommandService roomCommandService;
    @Autowired
    private RoomSaveRedisAdapter roomSaveRedisAdapter;
    @Autowired
    private RoomQueryRedisAdapter roomQueryRedisAdapter;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private RoomParticipantSaveAdapter roomParticipantSaveAdapter;

    private Room room;
    private Member member;
    private Member member2;

    @BeforeEach
    void init() {
        member = MemberFixture.createLeader();
        member2 = MemberFixture.createFollower();
        room = Room.create("투표방", 2, member);
        roomSaveRedisAdapter.save(room);
        roomParticipantSaveAdapter.joinRoom(room.getId(), member);
        roomParticipantSaveAdapter.joinRoom(room.getId(), member2);
    }

    @DisplayName("약속방이 정상적으로 생성된다.")
    @Test
    void createRoom() {
        String roomName = "Test Room";
        int capacity = 4;
        String hostNickname = "test";
        String hostProfile = "hostProfile";
        Member host = new Member(hostNickname, hostProfile, MemberRole.LEADER);

        Room createdRoom = roomCommandService.createRoom(roomName, capacity, host);

        assertAll("Room Create Test",
            () -> assertNotNull(createdRoom, "Room 객체는 null이 아니어야 합니다."),
            () -> assertNotNull(createdRoom.getId(), "약속방 ID는 null이 아니어야 합니다."),
            () -> assertEquals(roomName, createdRoom.getRoomName(), "약속방 이름이 일치해야 합니다."),
            () -> assertEquals(capacity, createdRoom.getCapacity(), "참여 인원 수가 일치해야 합니다."),
            () -> assertNotNull(createdRoom.getMember(), "방장 정보는 null이 아니어야 합니다."),
            () -> assertEquals(hostNickname, createdRoom.getMember().getNickname(),
                "방장 닉네임이 일치해야 합니다."),
            () -> assertEquals(hostProfile, createdRoom.getMember().getProfile(),
                "방장 프로필이 일치해야 합니다."),
            () -> assertEquals(MemberRole.LEADER, createdRoom.getMember().getRole(),
                "방장 역할은 Leader여야 합니다.")
        );
    }

    @DisplayName("모든 인원이 출발지를 입력하면 투표가 시작되고, 마감 시간이 갱신된다.")
    @Test
    void startVote_whenAllLocationsInput() {
        // given
        Location location = new Location(
            room.getId(),
            member.getMemberId(),
            PointFixture.create(),
            "서울시 마포구"
        );
        Location location2 = new Location(
            room.getId(),
            member2.getMemberId(),
            PointFixture.create(),
            "서울시 강서구"
        );
        locationRepository.save(location);
        locationRepository.save(location2);
        LocalDateTime now = LocalDateTime.now().withNano(0);

        // when
        roomCommandService.startVote(room.getId(), now);

        // then
        Room updatedRoom = roomQueryRedisAdapter.findRoomById(room.getId()).get();

        assertAll(
            () -> assertThat(updatedRoom.getStatus()).isEqualTo(RoomStatus.VOTE),
            () -> assertThat(updatedRoom.getVoteLimitDateTime()).isEqualTo(now.plusHours(12))
        );
    }

    @DisplayName("투표 종료 시 방 상태가 VOTE_RESULT로 변경된다.")
    @Test
    void closeVote() {
        // given
        room.startVote();
        roomSaveRedisAdapter.update(room);

        // when
        roomCommandService.closeVote(room.getId());

        // then
        Room updatedRoom = roomQueryRedisAdapter.findRoomById(room.getId()).get();

        assertThat(updatedRoom.getStatus()).isEqualTo(RoomStatus.VOTE_RESULT);
    }
}
