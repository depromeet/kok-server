package com.kok.kokapi.station.application.config;

import com.kok.kokapi.station.application.service.StationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StationsConfig {

    @Bean
    CommandLineRunner initStations(StationService stationService) {
        return args -> stationService.saveStations();
    }
}
