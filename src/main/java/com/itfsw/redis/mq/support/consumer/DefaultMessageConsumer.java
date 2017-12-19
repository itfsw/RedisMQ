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

package com.itfsw.redis.mq.support.consumer;

import com.itfsw.redis.mq.MessageConsumer;
import com.itfsw.redis.mq.MessageQueue;
import com.itfsw.redis.mq.support.consumer.handler.QueueMessageExpiredHandler;
import com.itfsw.redis.mq.support.consumer.handler.QueueMessageFailureHandler;
import com.itfsw.redis.mq.support.consumer.handler.QueueMessageSuccessHandler;
import com.itfsw.redis.mq.support.consumer.handler.QueueMessageTimeoutHandler;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * ---------------------------------------------------------------------------
 * 默认 MessageConsumer 实现
 * ---------------------------------------------------------------------------
 * @author: hewei
 * @time:2017/11/20 13:26
 * ---------------------------------------------------------------------------
 */
public class DefaultMessageConsumer<T> implements MessageConsumer<T> {
    @Autowired
    private MessageQueue<T> queue;  // 消息队列

    private QueueMessageSuccessHandler successHandler;    // 处理成功Handler
    private QueueMessageFailureHandler failureHandler;    // 处理失败Handler
    private QueueMessageExpiredHandler expiredHandler;  // 消息过期处理Handler
    private QueueMessageTimeoutHandler timeoutHandler;  // 消息处理超时Handler

    @Override
    public MessageQueue<T> getQueue() {
        return queue;
    }
}
