package org.shooong.push.global.exception;
public class RedisOperationException extends RuntimeException {
    public RedisOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
