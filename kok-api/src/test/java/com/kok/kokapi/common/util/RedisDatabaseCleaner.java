package com.kok.kokapi.common.util;

import java.util.Objects;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisDatabaseCleaner implements DatabaseCleaner {

    private final RedisTemplate<String, ?> redisTemplate;

    public RedisDatabaseCleaner(RedisTemplate<String, ?> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void cleanUp() {
        Objects.requireNonNull(redisTemplate.getConnectionFactory())
            .getConnection()
            .serverCommands()
            .flushDb();
    }
}
