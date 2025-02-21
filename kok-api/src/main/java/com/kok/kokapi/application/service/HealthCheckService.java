package com.kok.kokapi.application.service;

import org.springframework.stereotype.Service;

@Service
public class HealthCheckService {

    public String checkHealth() {
        return "OK";
    }
}
