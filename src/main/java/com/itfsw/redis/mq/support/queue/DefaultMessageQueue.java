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

package com.itfsw.redis.mq.support.queue;

import com.itfsw.redis.mq.MessageQueue;
import com.itfsw.redis.mq.model.MessageWrapper;
import com.itfsw.redis.mq.redis.RedisOperations;
import org.springframework.beans.factory.InitializingBean;

/**
 * ---------------------------------------------------------------------------
 *
 * ---------------------------------------------------------------------------
 * @author: hewei
 * @time:2017/11/17 16:12
 * ---------------------------------------------------------------------------
 */
public class DefaultMessageQueue<T> implements MessageQueue<T>, InitializingBean{
    private RedisOperations redisOps;   // Redis 操作对象
    private String queueName = "default";   // 队列名称
    private MessageQueue handlerQueue;  // 处理队列

    /**
     * 构造函数
     * @param redisOps
     */
    public DefaultMessageQueue(RedisOperations redisOps) {
        this.redisOps = redisOps;
    }

    /**
     * Setter method for property <tt>queueName</tt>.
     * @param queueName value to be assigned to property queueName
     * @author hewei
     */
    @Override
    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }


    /**
     * Getter method for property <tt>queueName</tt>.
     * @return property value of queueName
     * @author hewei
     */
    @Override
    public String getQueueName() {
        return queueName;
    }

    @Override
    public MessageQueue<T> handlerQueue() {
        return handlerQueue;
    }

    @Override
    public RedisOperations redisOps() {
        return redisOps;
    }

    @Override
    public <T1> void add(MessageWrapper<T1> message) {
        redisOps.opsForList().leftPush(queueName, message);
    }

    @Override
    public <T1> void preAdd(MessageWrapper<T1> message) {
        redisOps.opsForList().rightPush(queueName, message);
    }

    @Override
    public MessageWrapper<T> poll() {
        return redisOps.opsForList().rightPop(queueName);
    }

    @Override
    public MessageWrapper<T> pollTo(MessageQueue<T> queue) {
        if (queue.redisOps().equals(redisOps)) {
            return redisOps.opsForList().rightPopAndLeftPush(queueName, queue.getQueueName());
        } else {
            MessageWrapper<T> message = redisOps.opsForList().rightPop(queueName);
            queue.add(message);
            return message;
        }
    }

    @Override
    public long size() {
        return redisOps.opsForList().size(queueName);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        handlerQueue = new DefaultMessageQueue(redisOps);
        handlerQueue.setQueueName(queueName + "-handler");
    }
}
