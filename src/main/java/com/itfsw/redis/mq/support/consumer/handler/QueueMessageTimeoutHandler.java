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

package com.itfsw.redis.mq.support.consumer.handler;

import com.itfsw.redis.mq.MessageQueue;
import com.itfsw.redis.mq.model.MessageWrapper;

/**
 * ---------------------------------------------------------------------------
 * 消息处理成功
 * ---------------------------------------------------------------------------
 * @author: hewei
 * @time:2017/11/17 22:35
 * ---------------------------------------------------------------------------
 */
public interface QueueMessageTimeoutHandler<T> {
    /**
     * 接收处理消息
     * @param messageQueue
     * @param messageWrapper
     */
    void onMessage(MessageQueue<T> messageQueue, MessageWrapper<T> messageWrapper);
}
