package com.itfsw.redis.mq.exception;

/**
 * ---------------------------------------------------------------------------
 *
 * ---------------------------------------------------------------------------
 * @author: hewei
 * @time:2017/12/25 10:31
 * ---------------------------------------------------------------------------
 */
public class MessageHandlerException extends RedisMqException {
    public MessageHandlerException() {
    }

    public MessageHandlerException(String message) {
        super(message);
    }

    public MessageHandlerException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageHandlerException(Throwable cause) {
        super(cause);
    }

    public MessageHandlerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
