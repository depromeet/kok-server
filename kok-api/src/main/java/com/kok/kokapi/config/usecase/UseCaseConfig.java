package com.kok.kokapi.config.usecase;

import com.kok.kokapi.room.application.service.RoomService;
import com.kok.kokcore.application.port.out.RoomRepository;
import com.kok.kokcore.usecase.CreateRoomUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {
    @Bean
    public CreateRoomUseCase createRoomUseCase(RoomRepository roomRepository) {
        return new RoomService(roomRepository);
    }
}
