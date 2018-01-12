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

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * ---------------------------------------------------------------------------
 * Publisher
 * ---------------------------------------------------------------------------
 * @author: hewei
 * @time:2017/11/13 11:03
 * ---------------------------------------------------------------------------
 */
public interface MessagePublisher<T> extends InitializingBean, DisposableBean {
    /**
     * 发送消息
     * @param message
     * @return 消息Id
     */
    String send(T message);

    /**
     * 创建消息发送器
     * @param message
     * @return
     */
    MessageSender<T> create(T message);

    /**
     * 获取频道
     * @return
     */
    MessageChannel<T> getChannel();
}
