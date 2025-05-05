package com.kok.kokapi.common.exception;

import com.kok.kokapi.common.error.ErrorCode;
import com.kok.kokapi.common.response.ApiResponseDto;
import jakarta.validation.ConstraintViolationException;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleConstraintViolationException(
        ConstraintViolationException ex) {
        String message = ex.getConstraintViolations()
            .stream()
            .map(cv -> cv.getPropertyPath() + " " + cv.getMessage())
            .collect(Collectors.joining(", "));
        return ResponseEntity.badRequest()
            .body(ApiResponseDto.error(ErrorCode.INVALID_INPUT, message));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors()
            .stream()
            .map(fe -> fe.getField() + " " + fe.getDefaultMessage())
            .collect(Collectors.joining(", "));
        return ResponseEntity.badRequest()
            .body(ApiResponseDto.error(ErrorCode.INVALID_INPUT, message));
    }

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<ApiResponseDto<Void>> handleBadRequestException(RuntimeException ex) {
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
