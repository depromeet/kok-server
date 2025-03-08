package com.kok.kokapi.common.template;

import com.redis.testcontainers.RedisContainer;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;

public abstract class ContainerBaseTest {

    private static final int REDIS_PORT = 6379;

    private static final MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.4")
        .withDatabaseName("kok-db")
        .withUsername("root")
        .withPassword("1234");

    private static final RedisContainer redisContainer = new RedisContainer("redis:7.0"
    );

    static {
        mysqlContainer.start();
        redisContainer.start();
    }

    @DynamicPropertySource
    private static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.driver-class-name", mysqlContainer::getDriverClassName);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);

        registry.add("spring.data.redis.host", redisContainer::getHost);
        registry.add("spring.data.redis.port", () -> redisContainer.getMappedPort(REDIS_PORT));

        registry.add("spring.flyway.enabled", () -> true);
        registry.add("spring.flyway.baseline-on-migrate", () -> true);
    }
}
