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

import com.itfsw.redis.mq.MessagePublisher;
import com.itfsw.redis.mq.MessageSender;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * ---------------------------------------------------------------------------
 *
 * ---------------------------------------------------------------------------
 * @author: hewei
 * @time:2017/11/15 16:45
 * ---------------------------------------------------------------------------
 */
public class DefaultMessagePublisher<T> implements MessagePublisher {
    private RedisTemplate template;

    @Override
    public void send(Object message) {
        template.convertAndSend("ss", message);
    }

    @Override
    public MessageSender create(Object message) {
        return null;
    }
}
