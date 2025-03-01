package com.kok.kokapi.config;

import com.kok.kokapi.station.adapter.out.external.FakeStationClient;
import com.kok.kokcore.room.application.port.out.SaveRoomPort;
import com.kok.kokcore.room.usecase.JoinRoomUseCase;
import com.kok.kokcore.station.application.port.out.LoadStationsPort;
import org.mockito.Mockito;
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

    @Bean
    public SaveRoomPort saveRoomPort() {
        return Mockito.mock(SaveRoomPort.class);
    }

    @Bean
    public JoinRoomUseCase joinRoomUseCase() {
        return Mockito.mock(JoinRoomUseCase.class);
    }
}
