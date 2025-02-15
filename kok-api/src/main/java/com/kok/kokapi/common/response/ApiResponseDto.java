package com.kok.kokapi.common.response;


import com.kok.kokapi.common.error.ErrorCode;
import org.springframework.http.HttpStatus;

public record ApiResponseDto<T>(int code, String message, T data) {

    public static <T> ApiResponseDto<T> success(T data) {
        return new ApiResponseDto<>(HttpStatus.OK.value(), "Success", data);
    }

    public static ApiResponseDto<Void> error(ErrorCode errorCode) {
        return new ApiResponseDto<>(errorCode.getStatus().value(), errorCode.getMessage(), null);
    }

    public static ApiResponseDto<Void> error(ErrorCode errorCode, String customMessage) {
        return new ApiResponseDto<>(errorCode.getStatus().value(), customMessage, null);
    }
}
