package com.kok.kokapi.common.template;

import com.kok.kokapi.config.DataJpaTestConfig;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@Import({DataJpaTestConfig.class})
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestPropertySource(properties = {"spring.config.location = classpath:application-test.yml"})
@Profile("test")
public abstract class RepositoryTest extends ContainerBaseTest{

}
