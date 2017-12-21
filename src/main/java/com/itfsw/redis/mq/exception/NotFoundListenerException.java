package com.itfsw.redis.mq.exception;

/**
 * ---------------------------------------------------------------------------
 *
 * ---------------------------------------------------------------------------
 * @author: hewei
 * @time:2017/12/21 19:02
 * ---------------------------------------------------------------------------
 */
public class NotFoundListenerException extends RedisMqException {
    public NotFoundListenerException(String message) {
        super(message);
    }
}
