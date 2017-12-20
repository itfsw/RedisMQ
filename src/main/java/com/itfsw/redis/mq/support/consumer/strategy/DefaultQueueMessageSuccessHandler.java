package com.itfsw.redis.mq.support.consumer.strategy;

import com.itfsw.redis.mq.MessageQueue;
import com.itfsw.redis.mq.model.MessageWrapper;
import com.itfsw.redis.mq.support.consumer.handler.QueueMessageSuccessHandler;

/**
 * ---------------------------------------------------------------------------
 *
 * ---------------------------------------------------------------------------
 * @author: hewei
 * @time:2017/12/20 10:06
 * ---------------------------------------------------------------------------
 */
public class DefaultQueueMessageSuccessHandler implements QueueMessageSuccessHandler {
    @Override
    public void onMessage(MessageQueue messageQueue, MessageWrapper messageWrapper) {
    }
}
