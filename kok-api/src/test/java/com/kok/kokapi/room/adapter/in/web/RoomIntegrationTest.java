package com.kok.kokapi.room.adapter.in.web;

import static org.hamcrest.Matchers.is;

import com.kok.kokapi.centroid.adapter.in.dto.request.LocationRequest;
import com.kok.kokapi.common.template.IntegrationTest;
import com.kok.kokapi.room.adapter.in.dto.request.CreateRoomRequest;
import com.kok.kokapi.room.adapter.in.dto.request.JoinRoomParticipantRequest;
import com.kok.kokapi.room.adapter.in.dto.response.JoinRoomResponse;
import com.kok.kokapi.room.adapter.in.dto.response.RoomCreateResponse;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

class RoomIntegrationTest extends IntegrationTest {

    @DisplayName("약속방 시나리오")
    @TestFactory
    Stream<DynamicTest> getRoomDetail() {
        AtomicReference<RoomCreateResponse> createRoomResponse = new AtomicReference<>();
        AtomicReference<JoinRoomResponse> joinRoomResponse = new AtomicReference<>();

        return Stream.of(
            DynamicTest.dynamicTest("약속방을 생성한다.",
                () -> createRoomResponse.set(createRoom(
                    new CreateRoomRequest("room", 2, "hostProfile.svg", "hostNickname")))),

            inputLocation("방장이 출발지 정보를 입력한다.", createRoomResponse),

            getRoomDetail("약속방 정보를 조회해보면 미참여자는 1명이고, isFulled는 false이다.", createRoomResponse, 1,
                false),

            DynamicTest.dynamicTest("팔로워가 약속방에 참여한다.",
                () -> joinRoomResponse.set(joinRoom(createRoomResponse.get().id(),
                    new JoinRoomParticipantRequest("profile", "follower")))),

            getRoomDetail("약속방 정보를 조회해보면 미참여자는 0명이고, isFulled는 true이다.", createRoomResponse, 0,
                true),

            checkVoteMode("아직 출발지 입력을 완료하지 않았기에 voteMode는 false이다.", createRoomResponse, false),

            inputLocation("팔로워가 출발지 정보를 입력한다.", createRoomResponse, joinRoomResponse),

            checkVoteMode("약속방 정보를 조회해보면 모든 참여자가 출발지 입력을 완료했기에 voteMode는 true이다.",
                createRoomResponse, true)
        );
    }

    private RoomCreateResponse createRoom(CreateRoomRequest request) {
        return RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(request)
            .when().post("/v1/api/rooms")
            .then().log().all()
            .assertThat().statusCode(201)
            .extract().body().jsonPath().getObject("data", RoomCreateResponse.class);
    }

    private JoinRoomResponse joinRoom(String roomId, JoinRoomParticipantRequest request) {
        return RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(request)
            .when().post("/v1/api/rooms/" + roomId + "/join")
            .then().log().all()
            .assertThat().statusCode(200)
            .extract().body().jsonPath().getObject("data", JoinRoomResponse.class);
    }

    private static DynamicTest inputLocation(String message,
        AtomicReference<RoomCreateResponse> createRoomResponse) {
        return DynamicTest.dynamicTest(message,
            () -> {
                String roomId = createRoomResponse.get().id();
                String memberId = createRoomResponse.get().member().id();

                RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(new LocationRequest(roomId, memberId, new BigDecimal("37"),
                        new BigDecimal("127")))
                    .when().post("/v1/api/locations")
                    .then().log().all()
                    .assertThat().statusCode(200);
            });
    }

    private static DynamicTest getRoomDetail(String message,
        AtomicReference<RoomCreateResponse> createRoomResponse, int nonParticipantCount,
        boolean expectedIsFulled) {
        return DynamicTest.dynamicTest(message,
            () -> {
                String roomId = createRoomResponse.get().id();
                RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .when().get("/v1/api/rooms/" + roomId)
                    .then().log().all()
                    .assertThat().statusCode(200)
                    .body("data.nonParticipantCount", is(nonParticipantCount))
                    .body("data.isFulled", is(expectedIsFulled));
            });
    }

    private static DynamicTest inputLocation(String message,
        AtomicReference<RoomCreateResponse> createRoomResponse,
        AtomicReference<JoinRoomResponse> joinRoomResponse) {
        return DynamicTest.dynamicTest(message,
            () -> {
                String roomId = createRoomResponse.get().id();
                String memberId = joinRoomResponse.get().id();

                RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(new LocationRequest(roomId, memberId, new BigDecimal("37"),
                        new BigDecimal("127")))
                    .when().post("/v1/api/locations")
                    .then().log().all()
                    .assertThat().statusCode(200);
            });
    }

    private static DynamicTest checkVoteMode(String message,
        AtomicReference<RoomCreateResponse> createRoomResponse, boolean expectedIsVoteMode) {
        return DynamicTest.dynamicTest(message,
            () -> {
                String roomId = createRoomResponse.get().id();
                RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .when().get("/v1/api/rooms/" + roomId + "/status")
                    .then().log().all()
                    .assertThat().statusCode(200)
                    .body("data.isVoteMode", is(expectedIsVoteMode));
            });
    }
}
