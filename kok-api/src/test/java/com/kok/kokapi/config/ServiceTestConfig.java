package com.kok.kokapi.config;

import com.kok.kokapi.station.adapter.out.external.FakeStationClient;
import com.kok.kokcore.station.application.port.out.LoadStationsPort;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class ServiceTestConfig {

    @Bean
    @Primary
    public LoadStationsPort loadStationsPort() {
        return new FakeStationClient();
    }
}
