package com.kok.kokapi.monitoring.adapter.in.web;

import com.kok.kokapi.config.annotion.V1Controller;
import com.kok.kokapi.monitoring.application.service.HealthCheckService;
import com.kok.kokapi.common.response.ApiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

@V1Controller
@RequiredArgsConstructor
public class HealthCheckController {

    private final HealthCheckService healthCheckService;

    @GetMapping("/health")
    public ApiResponseDto<String> checkHealth() {
        return ApiResponseDto.success(healthCheckService.checkHealth());
    }
}
