package com.itfsw.redis.mq.support.consumer.strategy;

import com.itfsw.redis.mq.MessageQueue;
import com.itfsw.redis.mq.model.MessageWrapper;
import com.itfsw.redis.mq.support.consumer.handler.QueueMessageExpiredHandler;

/**
 * ---------------------------------------------------------------------------
 *
 * ---------------------------------------------------------------------------
 * @author: hewei
 * @time:2017/12/20 13:35
 * ---------------------------------------------------------------------------
 */
public class DefaultQueueMessageExpiredHandler implements QueueMessageExpiredHandler {
    @Override
    public void onMessage(MessageQueue messageQueue, MessageWrapper messageWrapper) {

    }
}
