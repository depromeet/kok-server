package com.kok.kokapi.common.config;

import com.kok.kokapi.station.adapter.out.external.FakeStationClient;
import com.kok.kokcore.station.application.port.out.LoadStationsPort;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class ServiceTestConfig {

    @Bean
    public LoadStationsPort loadStationsPort() {
        return new FakeStationClient();
    }
}
