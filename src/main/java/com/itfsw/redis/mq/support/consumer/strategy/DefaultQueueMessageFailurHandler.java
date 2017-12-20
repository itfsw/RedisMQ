package com.itfsw.redis.mq.support.consumer.strategy;

import com.itfsw.redis.mq.MessageQueue;
import com.itfsw.redis.mq.model.MessageWrapper;
import com.itfsw.redis.mq.support.consumer.handler.QueueMessageFailureHandler;

/**
 * ---------------------------------------------------------------------------
 *
 * ---------------------------------------------------------------------------
 * @author: hewei
 * @time:2017/12/20 13:35
 * ---------------------------------------------------------------------------
 */
public class DefaultQueueMessageFailurHandler implements QueueMessageFailureHandler {
    @Override
    public void onMessage(MessageQueue messageQueue, MessageWrapper messageWrapper, Throwable e) {

    }
}
