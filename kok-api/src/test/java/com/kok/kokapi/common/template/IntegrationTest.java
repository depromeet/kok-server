package com.kok.kokapi.common.template;

import com.kok.kokapi.common.util.DatabaseCleanerExtension;
import com.kok.kokapi.config.StationTestConfiguration;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.TestPropertySource;

@ExtendWith(DatabaseCleanerExtension.class)
@Import(StationTestConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"spring.config.location = classpath:application-test.yml"})
@Profile("test")
public abstract class IntegrationTest extends ContainerBaseTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setPort() {
        RestAssured.port = port;
    }
}
