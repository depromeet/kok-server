package com.kok.kokapi.monitoring.adapter.in.web;

import com.kok.kokapi.monitoring.application.service.HealthCheckService;
import com.kok.kokapi.common.adapter.in.web.BaseController;
import com.kok.kokapi.common.response.ApiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HealthCheckController extends BaseController {

    private final HealthCheckService healthCheckService;

    @GetMapping("/health")
    public ApiResponseDto<String> checkHealth() {
        return ApiResponseDto.success(healthCheckService.checkHealth());
    }
}
