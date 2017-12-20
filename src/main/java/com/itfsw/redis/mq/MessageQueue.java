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

package com.itfsw.redis.mq;

import com.itfsw.redis.mq.model.MessageWrapper;
import com.itfsw.redis.mq.redis.RedisOperations;

/**
 * ---------------------------------------------------------------------------
 * Queue
 * ---------------------------------------------------------------------------
 * @author: hewei
 * @time:2017/11/13 11:01
 * ---------------------------------------------------------------------------
 */
public interface MessageQueue<T> {
    /**
     * 获取队列名称
     * @return
     */
    String getQueueName();

    /**
     * 获取处理队列
     * @return
     */
    MessageQueue<T> handlerQueue();

    /**
     * 设置队列名称
     * @return
     */
    void setQueueName(String queueName);

    /**
     * Redis操作
     * @return
     */
    RedisOperations redisOps();

    /**
     * 添加消息
     * @param message
     */
    <T> void add(MessageWrapper<T> message);

    /**
     * 添加到队列的前端
     * @param message
     */
    <T> void preAdd(MessageWrapper<T> message);

    /**
     * 删除返回队列元素
     * @return
     */
    MessageWrapper<T> poll();

    /**
     * 删除返回队列元素（并把元素加入新队列）
     * @param queue
     * @return
     */
    MessageWrapper<T> pollTo(MessageQueue<T> queue);

    /**
     * 消息队列大小
     * @return
     */
    long size();
}
