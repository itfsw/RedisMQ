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
import com.itfsw.redis.mq.exception.MessageHandlerException;
import com.itfsw.redis.mq.model.MessageWrapper;

/**
 * ---------------------------------------------------------------------------
 * 消息处理失败
 * ---------------------------------------------------------------------------
 * @author: hewei
 * @time:2017/11/17 22:35
 * ---------------------------------------------------------------------------
 */
public interface QueueMessageFailureHandler<T> {
    /**
     * 异常处理（消息处理异常）
     * @param queue
     * @param messageWrapper
     * @param e
     */
    void onMessageHandlerException(MessageQueue<T> queue, MessageWrapper<T> messageWrapper, MessageHandlerException e);

    /**
     * 异常处理（一些未定义的运行时异常）
     * @param queue
     * @param wrapper
     * @param e
     */
    void onMessageProgressException(MessageQueue<T> queue, MessageWrapper<T> wrapper, Throwable e);
}
