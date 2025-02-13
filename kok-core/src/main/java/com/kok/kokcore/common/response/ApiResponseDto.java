package com.kok.kokcore.common.response;

import com.kok.kokcore.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ApiResponseDto<T> {
    private final int code;
    private final String message;
    private final T data;

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
