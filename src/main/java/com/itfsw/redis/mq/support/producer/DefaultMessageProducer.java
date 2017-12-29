/*
 * Copyright (c) 2017.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.itfsw.redis.mq.support.producer;

import com.itfsw.redis.mq.MessageProducer;
import com.itfsw.redis.mq.MessageQueue;
import com.itfsw.redis.mq.MessageSender;
import com.itfsw.redis.mq.model.MessageWrapper;
import com.itfsw.redis.mq.redis.RedisOperations;
import com.itfsw.redis.mq.support.sender.AbstractMessageSender;

/**
 * ---------------------------------------------------------------------------
 * 默认 MessageProducer 实现
 * ---------------------------------------------------------------------------
 * @author: hewei
 * @time:2017/11/17 13:55
 * ---------------------------------------------------------------------------
 */
public class DefaultMessageProducer<T> implements MessageProducer<T> {
    private MessageQueue<T> queue;  // 消息队列

    /**
     * 构造函数
     * @param messageQueue
     */
    public DefaultMessageProducer(MessageQueue<T> messageQueue) {
        this.queue = messageQueue;
    }

    @Override
    public String send(T message) {
        return new DefaultMessageSender<T>(queue.redisOps()).withMessage(message).send();
    }

    @Override
    public MessageSender<T> create(T message) {
        return new DefaultMessageSender<T>(queue.redisOps()).withMessage(message);
    }

    @Override
    public MessageQueue<T> getQueue() {
        return queue;
    }

    /**
     * 消息发送器
     * @param <T>
     */
    private class DefaultMessageSender<T> extends AbstractMessageSender<T> {

        /**
         * 构造函数
         * @param redisOps
         */
        public DefaultMessageSender(RedisOperations redisOps) {
            super(redisOps);
        }

        @Override
        public void send(MessageWrapper<T> messageWrapper) {
            if (messageWrapper.getHighPriority()) {
                queue.preAdd(messageWrapper);
            } else {
                queue.add(messageWrapper);
            }
        }
    }
}
