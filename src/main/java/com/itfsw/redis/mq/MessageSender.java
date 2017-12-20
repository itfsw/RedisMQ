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

/**
 * ---------------------------------------------------------------------------
 * 消息发送器
 * ---------------------------------------------------------------------------
 * @author: hewei
 * @time:2017/11/16 13:34
 * ---------------------------------------------------------------------------
 */
public interface MessageSender<T> {
    /**
     * 消息过期时间
     * @param timeToLive
     * @return
     */
    MessageSender<T> withExpires(long timeToLive);

    /**
     * 高优先级
     * @param highPriority
     * @return
     */
    MessageSender<T> withHighPriority(boolean highPriority);

    /**
     * 发送
     */
    void send();
}
