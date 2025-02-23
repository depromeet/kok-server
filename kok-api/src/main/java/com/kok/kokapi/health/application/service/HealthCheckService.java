package com.kok.kokapi.health.application.service;

import org.springframework.stereotype.Service;

@Service
public class HealthCheckService {

    public String checkHealth() {
        return "OK";
    }
}
