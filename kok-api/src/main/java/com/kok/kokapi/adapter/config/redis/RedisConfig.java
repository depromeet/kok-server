package com.kok.kokapi.adapter.config.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Bean
    public RedisConnectionFactory redisStandaloneConnectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(redisHost, redisPort);
        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                // TODO: 추가사항 추가. (명령어 타임아웃, 종료 타임아웃)
                .build();
        return new LettuceConnectionFactory(redisConfig, clientConfig);
    }

//    // 페일오버 가능한 Redis Sentinel 설정
//    @Bean
//    public RedisConnectionFactory redisSentinelConnectionFactory() {
//        RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration()
//                .master("") // Sentinel에서 설정한 master 이름
//                .sentinel("host1", 1234) //Sentinel1 서버 정보
//                .sentinel("host2", 1234); // Sentinel2 서버 정보
//        return new LettuceConnectionFactory(sentinelConfig);
//    }


    @Bean(name = "redisTemplate")
    public RedisTemplate<UUID, Object> redisTemplate() {
        RedisTemplate<UUID, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisStandaloneConnectionFactory());

        // Key Serializer 설정 (UUID -> String 변환)
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        // Value Serializer 설정 (객체 -> JSON 변환)
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }
}
