package com.kok.kokapi.application.service;

import com.kok.kokapi.application.usecase.HealthCheckUseCase;
import org.springframework.stereotype.Service;

@Service
public class HealthCheckService implements HealthCheckUseCase {

    @Override
    public String checkHealth() {
        return "OK";
    }
}
