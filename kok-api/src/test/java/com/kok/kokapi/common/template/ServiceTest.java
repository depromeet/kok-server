package com.kok.kokapi.common.template;

import com.kok.kokapi.common.util.MySQLDatabaseCleanerExtension;
import com.kok.kokapi.config.ServiceTestConfig;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.TestPropertySource;

@ExtendWith(MySQLDatabaseCleanerExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Import({ServiceTestConfig.class})
@TestPropertySource(properties = {"spring.config.location = classpath:application-test.yml"})
@Profile("test")
public abstract class ServiceTest extends ContainerBaseTest{

}

