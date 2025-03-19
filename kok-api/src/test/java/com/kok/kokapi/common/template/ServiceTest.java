package com.kok.kokapi.common.template;

import com.kok.kokapi.common.util.DatabaseCleanerExtension;
import com.kok.kokapi.config.StationTestConfiguration;
import com.kok.kokapi.public_transportation.adapter.out.external.PublicTransportationClient;
import com.kok.kokapi.public_transportation.adapter.out.external.PublicTransportationComplexClient;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@ExtendWith(DatabaseCleanerExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Import({StationTestConfiguration.class})
@TestPropertySource(properties = {"spring.config.location = classpath:application-test.yml"})
@Profile("test")
public abstract class ServiceTest extends ContainerBaseTest{

    @MockitoBean
    protected PublicTransportationComplexClient publicTransportationComplexClient;
    @MockitoBean
    protected PublicTransportationClient publicTransportationClient;
}
