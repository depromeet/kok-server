package com.kok.kokapi.monitoring.application.service;

import org.springframework.stereotype.Service;

@Service
public class HealthCheckService {

    public String checkHealth() {
        return "OK";
    }
}
