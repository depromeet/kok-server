package com.kok.kokapi.common.template;

import com.kok.kokapi.config.DataJpaTestConfig;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({DataJpaTestConfig.class})
@AutoConfigureTestDatabase(replace = Replace.NONE)
public abstract class RepositoryTest extends ContainerBaseTest{

}
