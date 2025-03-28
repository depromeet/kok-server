package com.kok.kokapi.common.util;

import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

@Slf4j
public class RedisExecutor {

    /**
     * Redis 작업을 실행하고 예외 발생 시 fallback 값을 반환합니다.
     */
    public static <T> T runOrElseGet(String operationName, Supplier<T> operation, T fallbackValue) {
        try {
            return retry(operationName, operation);
        } catch (RedisConnectionFailureException e) {
            log.error("[Redis][{}] Connection failure. Retry or alert needed.", operationName, e);
        } catch (RedisSystemException e) {
            log.error("[Redis][{}] System error (serialization, etc).", operationName, e);
        } catch (DataAccessException e) {
            log.warn("[Redis][{}] Data access issue.", operationName, e);
        } catch (Exception e) {
            log.error("[Redis][{}] Unexpected exception.", operationName, e);
        }
        return fallbackValue;
    }

    /**
     * Redis 작업을 실행하고 실패 시 예외를 그대로 던집니다.
     */
    public static <T> T runOrThrow(String operationName, Supplier<T> operation) {
        try {
            return retry(operationName, operation);
        } catch (RedisConnectionFailureException e) {
            log.error("[Redis][{}] Connection failure.", operationName, e);
            throw e;
        } catch (RedisSystemException e) {
            log.error("[Redis][{}] System error (serialization, etc).", operationName, e);
            throw e;
        } catch (DataAccessException e) {
            log.warn("[Redis][{}] Data access issue.", operationName, e);
            throw e;
        } catch (Exception e) {
            log.error("[Redis][{}] Unexpected exception.", operationName, e);
            throw e;
        }
    }

    /**
     * 반환값이 없는 Redis 작업 실행 및 예외 발생 시 throw하기 위한 메서드입니다.
     */
    public static void runOrThrow(String operationName, Runnable operation) {
        runOrThrow(operationName, () -> {
            operation.run();
            return null;
        });
    }

    /**
     * Redis 연결 실패에 대해서만 재시도하고, 나머지는 즉시 처리
     */
    @Retryable(
        value = RedisConnectionFailureException.class,
        maxAttempts = 3,
        backoff = @Backoff(delay = 300)
    )
    protected static <T> T retry(String operationName, Supplier<T> operation) {
        log.debug("[Redis][{}] Retrying operation.", operationName);
        return operation.get();
    }
}
