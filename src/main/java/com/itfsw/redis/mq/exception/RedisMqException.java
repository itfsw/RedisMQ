package com.itfsw.redis.mq.exception;

/**
 * ---------------------------------------------------------------------------
 *
 * ---------------------------------------------------------------------------
 * @author: hewei
 * @time:2017/12/20 11:17
 * ---------------------------------------------------------------------------
 */
public class RedisMqException extends RuntimeException {
    public RedisMqException() {
    }

    public RedisMqException(String message) {
        super(message);
    }

    public RedisMqException(String message, Throwable cause) {
        super(message, cause);
    }

    public RedisMqException(Throwable cause) {
        super(cause);
    }

    public RedisMqException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
