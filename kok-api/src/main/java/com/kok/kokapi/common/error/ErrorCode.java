package com.kok.kokapi.common.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 400 BAD REQUEST
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "입력값이 올바르지 않습니다."),
    MISSING_PARAMETER(HttpStatus.BAD_REQUEST, "필수 파라미터가 누락되었습니다."),
    INVALID_FORMAT(HttpStatus.BAD_REQUEST, "입력값의 형식이 올바르지 않습니다."),
    EXCEEDED_USAGE_LIMIT(HttpStatus.BAD_REQUEST, "호출 가능 횟수가 초과되었습니다."),
    REQUEST_EXPIRED(HttpStatus.BAD_REQUEST, "요청이 만료되었습니다."),
    UNSUPPORTED_OPERATION(HttpStatus.BAD_REQUEST, "지원하지 않는 기능입니다."),

    // 401 UNAUTHORIZED
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    TOKEN_MALFORMED(HttpStatus.UNAUTHORIZED, "토큰 형식이 올바르지 않습니다."),
    SESSION_EXPIRED(HttpStatus.UNAUTHORIZED, "세션이 만료되었습니다."),

    // 403 FORBIDDEN
    FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없습니다."),
    IP_BLOCKED(HttpStatus.FORBIDDEN, "해당 IP는 차단되었습니다."),
    INSUFFICIENT_PERMISSIONS(HttpStatus.FORBIDDEN, "해당 요청을 수행할 권한이 없습니다."),


    // 404 NOT FOUND
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다."),
    ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "약속방을 찾을 수 없습니다."),

    // 409 CONFLICT
    CONFLICT(HttpStatus.CONFLICT, "충돌이 발생했습니다."),
    VERSION_CONFLICT(HttpStatus.CONFLICT, "버전 충돌이 발생했습니다. 최신 데이터를 확인해주세요."),


    // 500 INTERNAL SERVER ERROR
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "현재 서비스를 이용할 수 없습니다."),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 오류가 발생했습니다."),
    EXTERNAL_API_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "외부 API 호출 중 오류가 발생했습니다."),
    UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 오류가 발생했습니다."),
    TIMEOUT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "요청 시간이 초과되었습니다.");

    private final HttpStatus status;
    private final String message;
}
