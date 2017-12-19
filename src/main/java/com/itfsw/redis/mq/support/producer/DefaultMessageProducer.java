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
import com.itfsw.redis.mq.utils.SnowflakeIdWorker;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * ---------------------------------------------------------------------------
 * 默认 MessageProducer 实现
 * ---------------------------------------------------------------------------
 * @author: hewei
 * @time:2017/11/17 13:55
 * ---------------------------------------------------------------------------
 */
public class DefaultMessageProducer<T> implements MessageProducer<T> {
    @Autowired
    private MessageQueue<T> queue;  // 消息队列

    /**
     * 构造函数
     * @param messageQueue
     */
    public DefaultMessageProducer(MessageQueue<T> messageQueue) {
        this.queue = messageQueue;
    }

    @Override
    public void send(T message) {
        new DefaultMessageSender<>(message).send();
    }

    @Override
    public MessageSender<T> create(T message) {
        return new DefaultMessageSender<>(message);
    }

    @Override
    public MessageQueue<T> getQueue() {
        return queue;
    }

    /**
     * 消息发送器
     * @param <T>
     */
    private class DefaultMessageSender<T> implements MessageSender<T> {
        private T message;  // 消息
        private String messageId;   // 消息ID
        private long expires = -1L;   // 有效期
        private boolean highPriority = false;   // 高优先级

        /**
         * 构造函数
         * @param message
         */
        public DefaultMessageSender(T message) {
            this.message = message;
        }

        @Override
        public MessageSender<T> withMessageId(String messageId) {
            this.messageId = messageId;
            return this;
        }

        @Override
        public MessageSender<T> withExpires(long timeToLive) {
            this.expires = timeToLive;
            return this;
        }

        @Override
        public MessageSender<T> withHighPriority(boolean highPriority) {
            this.highPriority = highPriority;
            return this;
        }

        @Override
        public void send() {
            MessageWrapper<T> messageWrapper = new MessageWrapper<>();
            messageWrapper.setMessage(message);
            messageWrapper.setMessageId(messageId == null ? SnowflakeIdWorker.getIdWorker().nextPerfectId() : messageId);
            messageWrapper.setExpires(expires < 0 ? -1 : expires);
            messageWrapper.setHighPriority(highPriority);
            messageWrapper.setCreateTime(queue.redisOps().time());
            if (messageWrapper.getHighPriority()) {
                queue.preAdd(messageWrapper);
            } else {
                queue.add(messageWrapper);
            }
        }
    }
}
