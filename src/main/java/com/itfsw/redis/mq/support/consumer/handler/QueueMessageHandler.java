package com.itfsw.redis.mq.support.consumer.handler;

import com.itfsw.redis.mq.MessageQueue;
import com.itfsw.redis.mq.model.MessageWrapper;

/**
 * ---------------------------------------------------------------------------
 *
 * ---------------------------------------------------------------------------
 * @author: hewei
 * @time:2017/12/20 9:42
 * ---------------------------------------------------------------------------
 */
public interface QueueMessageHandler<T> {
    /**
     * 接收处理消息
     * @param messageQueue
     * @param messageWrapper
     */
    void onMessage(MessageQueue<T> messageQueue, MessageWrapper<T> messageWrapper);
}
