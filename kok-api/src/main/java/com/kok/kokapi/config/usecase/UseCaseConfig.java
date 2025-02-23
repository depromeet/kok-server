package com.kok.kokapi.config.usecase;

import com.kok.kokapi.room.application.service.RoomCommandService;
import com.kok.kokcore.room.application.port.out.SaveRoomPort;
import com.kok.kokcore.room.usecase.CreateRoomUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {
    @Bean
    public CreateRoomUseCase createRoomUseCase(SaveRoomPort saveRoomPort) {
        return new RoomCommandService(saveRoomPort);
    }
}
