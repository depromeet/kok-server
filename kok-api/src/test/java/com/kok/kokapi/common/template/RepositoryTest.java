package com.kok.kokapi.common.template;

import com.kok.kokapi.common.util.DatabaseCleanerExtension;
import com.kok.kokapi.config.StationTestConfiguration;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.TestPropertySource;

@ExtendWith(DatabaseCleanerExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Import({StationTestConfiguration.class})
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestPropertySource(properties = {"spring.config.location = classpath:application-test.yml"})
@Profile("test")
public abstract class RepositoryTest extends ContainerBaseTest{

}
