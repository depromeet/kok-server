package com.kok.kokapi.adapter.inbound.rest;

import com.kok.kokapi.application.usecase.HealthCheckUseCase;
import com.kok.kokapi.common.response.ApiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HealthCheckController extends BaseController {

    private final HealthCheckUseCase healthCheckUseCase;

    @GetMapping("/health")
    public ApiResponseDto<String> checkHealth() {
        return ApiResponseDto.success(healthCheckUseCase.checkHealth());
    }
}
