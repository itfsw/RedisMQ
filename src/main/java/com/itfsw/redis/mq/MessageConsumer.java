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

import com.itfsw.redis.mq.support.consumer.handler.QueueMessageExpiredHandler;
import com.itfsw.redis.mq.support.consumer.handler.QueueMessageFailureHandler;
import com.itfsw.redis.mq.support.consumer.handler.QueueMessageSuccessHandler;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * ---------------------------------------------------------------------------
 * 消费者
 * ---------------------------------------------------------------------------
 * @author: hewei
 * @time:2017/11/13 10:53
 * ---------------------------------------------------------------------------
 */
public interface MessageConsumer<T> extends InitializingBean, DisposableBean{
    /**
     * 获取队列
     * @return
     */
    MessageQueue<T> getQueue();

    /**
     * 设置监听器
     * @param listener
     */
    void setMessageListener(MessageListener<T> listener);

    /**
     * 开启consumer
     */
    void startConsumer();

    /**
     * 结束consumer
     */
    void stopConsumer();

    /**
     * 设置成功handler
     * @param successHandler
     */
    void setSuccessHandler(QueueMessageSuccessHandler successHandler);

    /**
     * 设置失败handler
     * @param failureHandler
     */
    void setFailureHandler(QueueMessageFailureHandler failureHandler);

    /**
     * 设置过期handler
     * @param expiredHandler
     */
    void setExpiredHandler(QueueMessageExpiredHandler expiredHandler);

}
