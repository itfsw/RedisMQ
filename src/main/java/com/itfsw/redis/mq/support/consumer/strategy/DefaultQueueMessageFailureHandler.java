package com.itfsw.redis.mq.support.consumer.strategy;

import com.itfsw.redis.mq.MessageQueue;
import com.itfsw.redis.mq.exception.MessageHandlerException;
import com.itfsw.redis.mq.model.MessageWrapper;
import com.itfsw.redis.mq.support.consumer.handler.QueueMessageFailureHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ---------------------------------------------------------------------------
 *
 * ---------------------------------------------------------------------------
 * @author: hewei
 * @time:2017/12/20 13:35
 * ---------------------------------------------------------------------------
 */
public class DefaultQueueMessageFailureHandler implements QueueMessageFailureHandler {
    private final static Logger log = LoggerFactory.getLogger(DefaultQueueMessageFailureHandler.class);

    @Override
    public void onMessageHandlerException(MessageQueue queue, MessageWrapper messageWrapper, MessageHandlerException e) {
        log.error("消息处理异常", e);
    }

    @Override
    public void onMessageProgressException(MessageQueue queue, MessageWrapper wrapper, Throwable e) {
        // do nothing
    }
}
