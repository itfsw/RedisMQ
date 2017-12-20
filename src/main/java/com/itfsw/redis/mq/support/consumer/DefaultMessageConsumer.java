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
import com.itfsw.redis.mq.MessageListener;
import com.itfsw.redis.mq.MessageQueue;
import com.itfsw.redis.mq.model.MessageWrapper;
import com.itfsw.redis.mq.support.consumer.handler.QueueMessageExpiredHandler;
import com.itfsw.redis.mq.support.consumer.handler.QueueMessageFailureHandler;
import com.itfsw.redis.mq.support.consumer.handler.QueueMessageSuccessHandler;
import com.itfsw.redis.mq.support.consumer.handler.QueueMessageTimeoutHandler;
import com.itfsw.redis.mq.support.consumer.strategy.MultiThreadingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * ---------------------------------------------------------------------------
 * 默认 MessageConsumer 实现
 * ---------------------------------------------------------------------------
 * @author: hewei
 * @time:2017/11/20 13:26
 * ---------------------------------------------------------------------------
 */
public class DefaultMessageConsumer<T> implements MessageConsumer<T>, InitializingBean, DisposableBean {
    private static final Logger log = LoggerFactory.getLogger(MultiThreadingStrategy.class);

    @Autowired
    private MessageQueue<T> queue;  // 消息队列
    private MessageListener<T> messageListener; // 消息处理

    private QueueMessageSuccessHandler successHandler;    // 处理成功Handler
    private QueueMessageFailureHandler failureHandler;    // 处理失败Handler
    private QueueMessageExpiredHandler expiredHandler;  // 消息过期处理Handler
    private QueueMessageTimeoutHandler timeoutHandler;  // 消息处理超时Handler

    private int threadsNum = 1; // 线程数量
    private MultiThreadingStrategy messageHandlerThread;    // 消息处理线程
    private MultiThreadingStrategy messageCheckCircleThread;    // 消息检查线程


    /**
     * 构造函数
     * @param queue
     */
    public DefaultMessageConsumer(MessageQueue<T> queue) {
        this.queue = queue;
    }

    /**
     * 获取队列
     * @return
     */
    @Override
    public MessageQueue<T> getQueue() {
        return queue;
    }

    /**
     * 开启consumer
     * @param threads
     */
    @Override
    public void startConsumer(int threads) {
        this.threadsNum = threads;

        // 消息处理
        this.messageHandlerThread = new MultiThreadingStrategy(threads);
        this.messageHandlerThread.start(queue.getQueueName(), new Runnable() {
            @Override
            public void run() {
                MessageWrapper<T> wrapper = null;
                try {
                    wrapper = queue.pollTo(queue.handlerQueue());
                    long time = queue.redisOps().time();
                    wrapper.setExcuteTime(time);
                    // 1. 判断过期
                    if (wrapper.getExpires() > -1 && wrapper.getCreateTime() + wrapper.getExpires() > time) {
                        expiredHandler.onMessage(queue, wrapper);
                    } else {
                        // 2. 执行
                        messageListener.onMessage(wrapper);
                        // 3. 执行成功
                        successHandler.onMessage(queue, wrapper);
                    }
                } catch (Throwable e) {
                    failureHandler.onMessage(queue, wrapper, e);
                }
            }
        });

        // 消息检查
        this.messageCheckCircleThread = new MultiThreadingStrategy(1);
        this.messageCheckCircleThread.start(queue.handlerQueue().getQueueName(), new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    /**
     * 结束consumer
     */
    @Override
    public void stopConsumer() {
        this.messageHandlerThread.stop();
        this.messageCheckCircleThread.stop();
    }

    /**
     * Setter method for property <tt>messageListener</tt>.
     * @param messageListener value to be assigned to property messageListener
     * @author hewei
     */
    @Override
    public void setMessageListener(MessageListener<T> messageListener) {
        this.messageListener = messageListener;
    }

    /**
     * Setter method for property <tt>successHandler</tt>.
     * @param successHandler value to be assigned to property successHandler
     * @author hewei
     */
    @Override
    public void setSuccessHandler(QueueMessageSuccessHandler successHandler) {
        this.successHandler = successHandler;
    }

    /**
     * Setter method for property <tt>failureHandler</tt>.
     * @param failureHandler value to be assigned to property failureHandler
     * @author hewei
     */
    @Override
    public void setFailureHandler(QueueMessageFailureHandler failureHandler) {
        this.failureHandler = failureHandler;
    }

    /**
     * Setter method for property <tt>expiredHandler</tt>.
     * @param expiredHandler value to be assigned to property expiredHandler
     * @author hewei
     */
    @Override
    public void setExpiredHandler(QueueMessageExpiredHandler expiredHandler) {
        this.expiredHandler = expiredHandler;
    }

    /**
     * Setter method for property <tt>timeoutHandler</tt>.
     * @param timeoutHandler value to be assigned to property timeoutHandler
     * @author hewei
     */
    @Override
    public void setTimeoutHandler(QueueMessageTimeoutHandler timeoutHandler) {
        this.timeoutHandler = timeoutHandler;
    }

    @Override
    public void destroy() throws Exception {
        stopConsumer();
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        // 开启线程
        if (messageHandlerThread == null) {
            startConsumer(1);
        }
    }
}
