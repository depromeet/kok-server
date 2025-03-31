package com.kok.kokapi.vote.adapter.out.persistence;

import com.kok.kokapi.common.util.RedisExecutor;
import com.kok.kokcore.vote.domain.Vote;
import com.kok.kokcore.vote.domain.vo.VoteStatus;
import com.kok.kokcore.vote.port.out.LoadVotePort;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class VoteQueryRedisAdapter implements LoadVotePort {

    private static final String MEMBER_VOTE_KEY_FORMAT = "vote:%s:%s";
    private static final String VOTE_STATUS_VOTE_KEY_FORMAT = "%s:%s:%d";
    private static final int MAX_COUNT = 20;

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean isExistsByRoomIdAndMemberId(String roomId, String memberId) {
        String key = getMemberVoteKey(roomId, memberId);
        return RedisExecutor.runOrElseGet("isExistsByRoomIdAndMemberId", () ->
            !redisTemplate.opsForHash().entries(key).isEmpty(), false
        );
    }

    @Override
    public List<Vote> findAllByRoomIdAndMemberId(String roomId, String memberId) {
        String key = getMemberVoteKey(roomId, memberId);
        return RedisExecutor.runOrElseGet("findAllByRoomIdAndMemberId", () -> {
            Map<Object, Object> voteInfos = redisTemplate.opsForHash().entries(key);
            List<Vote> votes = new ArrayList<>(voteInfos.size());
            for (Entry<Object, Object> voteInfo : voteInfos.entrySet()) {
                Long stationId = getStationId(voteInfo);
                if (stationId == null) {
                    continue;
                }
                String voteStatus = String.valueOf(voteInfo.getValue());
                votes.add(new Vote(roomId, stationId, memberId, voteStatus));
            }
            return votes;
        }, List.of());
    }

    @Override
    public int countMembersByRoomId(String roomId) {
        String pattern = getMemberVoteKey(roomId, "*");
        return RedisExecutor.runOrElseGet("countMembersByRoomId", () -> {
            int count = 0;
            ScanOptions options = ScanOptions.scanOptions()
                .match(pattern)
                .count(MAX_COUNT)
                .build();

            try (Cursor<String> cursor = redisTemplate.scan(options)) {
                while (cursor.hasNext()) {
                    count++;
                    cursor.next();
                }
            }

            return count;
        }, 0);
    }

    @Override
    public List<String> findMemberIdsByRoomIdAndStationIdAndStatus(
        String roomId, long stationId, VoteStatus voteStatus) {
        String key = getVoteStatusVoteKey(voteStatus, roomId, stationId);
        Set<Object> memberIds = RedisExecutor.runOrElseGet(
            "findMembersByRoomIdAndStationIdAndStatus",
            () -> redisTemplate.opsForSet().members(key),
            Set.of()
        );
        return memberIds.stream().map(memberId -> (String) memberId).toList();
    }

    @Override
    public long getFirstStationIdByRoomIdAndVoteStatus(String roomId, VoteStatus voteStatus) {
        return 0;
    }

    private Long getStationId(Entry<Object, Object> voteInfo) {
        try {
            return Long.valueOf(voteInfo.getKey().toString());
        } catch (NumberFormatException e) {
            log.warn("Invalid stationId format in Redis: {}", voteInfo.getKey(), e);
            return null;
        }
    }

    private String getMemberVoteKey(String roomId, String memberId) {
        return String.format(MEMBER_VOTE_KEY_FORMAT, roomId, memberId);
    }

    private String getVoteStatusVoteKey(VoteStatus status, String roomId, long stationId) {
        return String.format(VOTE_STATUS_VOTE_KEY_FORMAT, status.getName(), roomId, stationId);
    }
}
