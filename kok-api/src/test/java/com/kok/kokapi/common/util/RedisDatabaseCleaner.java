package com.kok.kokapi.common.util;

import java.util.Objects;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
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
        RedisConnectionFactory connectionFactory = Objects.requireNonNull(
            redisTemplate.getConnectionFactory(),
            "RedisConnectionFactory must not be null"
        );

        try (RedisConnection connection = connectionFactory.getConnection()) {
            connection.serverCommands().flushDb();
        }
    }
}