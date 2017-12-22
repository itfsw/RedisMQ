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

package com.itfsw.redis.mq.support.publisher;

import com.itfsw.redis.mq.MessageChannel;
import com.itfsw.redis.mq.MessagePublisher;
import com.itfsw.redis.mq.MessageSender;
import com.itfsw.redis.mq.model.MessageWrapper;
import com.itfsw.redis.mq.support.sender.AbstractMessageSender;
import com.itfsw.redis.mq.support.sender.RedisBasedIdWorker;

/**
 * ---------------------------------------------------------------------------
 *
 * ---------------------------------------------------------------------------
 * @author: hewei
 * @time:2017/11/15 16:45
 * ---------------------------------------------------------------------------
 */
public class DefaultMessagePublisher<T> implements MessagePublisher<T> {
    private MessageChannel<T> channel;  // 频道
    private RedisBasedIdWorker idWorker;    // id生成器

    /**
     * 构造函数
     * @param channel
     */
    public DefaultMessagePublisher(MessageChannel<T> channel) {
        this.channel = channel;
    }

    @Override
    public String send(T message) {
        return new DefaultMessageSender<T>(idWorker).withMessage(message).send();
    }

    @Override
    public MessageSender<T> create(T message) {
        return new DefaultMessageSender<T>(idWorker).withMessage(message);
    }

    @Override
    public MessageChannel<T> getChannel() {
        return channel;
    }

    private class DefaultMessageSender<T> extends AbstractMessageSender<T> {

        /**
         * 构造函数
         * @param idWorker
         */
        public DefaultMessageSender(RedisBasedIdWorker idWorker) {
            super(idWorker);
        }

        @Override
        public void send(MessageWrapper<T> messageWrapper) {
            channel.redisOps().convertAndSend(channel.getChannel(), messageWrapper);
        }
    }
}
