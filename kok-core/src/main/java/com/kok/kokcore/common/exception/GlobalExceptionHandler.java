package com.kok.kokcore.common.exception;

import com.kok.kokcore.common.response.ApiResponseDto;
import com.kok.kokcore.common.error.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleBadRequestException(IllegalArgumentException ex) {
        return ResponseEntity.badRequest()
                .body(ApiResponseDto.error(ErrorCode.BAD_REQUEST, ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDto<Void>> handleGlobalException(Exception ex) {
        return ResponseEntity
                .status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(ApiResponseDto.error(ErrorCode.INTERNAL_SERVER_ERROR, ex.getMessage()));
    }
}
