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
                () -> createRoomResponse.set(RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(new CreateRoomRequest("room", 2, "hostProfile.svg", "hostNickname"))
                    .when().post("/v1/api/rooms")
                    .then().log().all()
                    .assertThat().statusCode(201)
                    .extract().body().jsonPath().getObject("data", RoomCreateResponse.class))),
            DynamicTest.dynamicTest("방장이 출발지 정보를 입력한다.",
                () -> inputLocation(createRoomResponse.get().id(), createRoomResponse.get().member().id())),
            DynamicTest.dynamicTest("약속방 정보를 조회해보면 아직 참여자가 다 들어오지 않았기 때문에 voteMode는 false이다.",
                () -> getRoomDetail(createRoomResponse.get().id(), false)),
            DynamicTest.dynamicTest("팔로워가 약속방에 참여한다.",
                () -> joinRoomResponse.set(RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(new JoinRoomParticipantRequest("profile", "follower"))
                    .when().post("/v1/api/rooms/"+createRoomResponse.get().id()+"/join")
                    .then().log().all()
                    .assertThat().statusCode(200)
                    .extract().body().jsonPath().getObject("data", JoinRoomResponse.class))),
            DynamicTest.dynamicTest("약속방 정보를 조회해보면 아직 출발지 입력을 완료하지 않았기에 voteMode는 false이다.",
                () -> getRoomDetail(createRoomResponse.get().id(), false)),
            DynamicTest.dynamicTest("팔로워가 출발지 정보를 입력한다.",
                () -> inputLocation(createRoomResponse.get().id(), joinRoomResponse.get().id())),
            DynamicTest.dynamicTest("약속방 정보를 조회해보면 모든 참여자가 출발지 입력을 완료했기에 voteMode는 true이다.",
                () -> getRoomDetail(createRoomResponse.get().id(), true))
        );
    }

    private static void inputLocation(String roomId, String memberId) {
        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(new LocationRequest(roomId, memberId, new BigDecimal("37.49794"), new BigDecimal("127.02758")))
            .when().post("/v1/api/locations")
            .then().log().all()
            .assertThat().statusCode(200);
    }

    private static void getRoomDetail(String roomId, boolean expected) {
        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .when().get("/v1/api/rooms/" + roomId)
            .then().log().all()
            .assertThat().statusCode(200).body("data.voteMode", is(expected));
    }
}