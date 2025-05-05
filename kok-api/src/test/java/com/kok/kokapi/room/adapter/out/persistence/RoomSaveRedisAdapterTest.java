package com.kok.kokapi.room.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kok.kokapi.common.template.RepositoryTest;
import com.kok.kokapi.fixture.MemberFixture;
import com.kok.kokapi.fixture.RoomFixture;
import com.kok.kokcore.room.domain.Room;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

class RoomSaveRedisAdapterTest extends RepositoryTest {

    private static final String ROOM_KEY_PREFIX = "room";

    @Autowired
    private RoomSaveRedisAdapter roomSaveRedisAdapter;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void updateRoom() throws Exception {
        // given
        Room room = RoomFixture.create(3, MemberFixture.createLeader());
        String key = buildKey(room.getId());
        roomSaveRedisAdapter.save(room);
        Long ttlBefore = redisTemplate.getExpire(key);
        LocalDateTime voteDeadlineBefore = room.getVoteLimitDateTime();

        Thread.sleep(1000);

        // when
        room.updateVoteDeadline(LocalDateTime.now());
        roomSaveRedisAdapter.update(room);

        // then
        String result = redisTemplate.opsForValue().get(key);
        Long ttlAfter = redisTemplate.getExpire(key);
        LocalDateTime voteDeadlineAfter = objectMapper.readValue(result, Room.class)
            .getVoteLimitDateTime();

        assertAll(
            () -> assertThat(ttlAfter).isGreaterThan(0L),
            () -> assertThat(ttlAfter).isLessThan(ttlBefore),
            () -> assertThat(voteDeadlineAfter).isBefore(voteDeadlineBefore)
        );
    }

    private String buildKey(String roomId) {
        return ROOM_KEY_PREFIX + ":" + roomId;
    }
}
