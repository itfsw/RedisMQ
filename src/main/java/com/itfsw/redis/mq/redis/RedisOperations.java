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

package com.itfsw.redis.mq.redis;

import com.itfsw.redis.mq.model.MessageWrapper;
import com.itfsw.redis.mq.redis.serializer.KeySerializer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.Assert;

/**
 * ---------------------------------------------------------------------------
 *
 * ---------------------------------------------------------------------------
 * @author: hewei
 * @time:2017/11/17 10:45
 * ---------------------------------------------------------------------------
 */
public class RedisOperations implements InitializingBean {
    private RedisConnectionFactory connectionFactory;
    private RedisSerializer keySerializer;
    private RedisSerializer valueSerializer;
    private RedisTemplate redisTemplate;

    /**
     * 构造函数
     * @param connectionFactory
     */
    public RedisOperations(RedisConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
        this.redisTemplate = new RedisTemplate();
    }

    /**
     * Getter method for property <tt>keySerializer</tt>.
     * @return property value of keySerializer
     * @author hewei
     */
    public RedisSerializer getKeySerializer() {
        return keySerializer;
    }

    /**
     * Setter method for property <tt>keySerializer</tt>.
     * @param keySerializer value to be assigned to property keySerializer
     * @author hewei
     */
    public void setKeySerializer(RedisSerializer keySerializer) {
        this.keySerializer = keySerializer;
    }

    /**
     * Getter method for property <tt>valueSerializer</tt>.
     * @return property value of valueSerializer
     * @author hewei
     */
    public RedisSerializer getValueSerializer() {
        return valueSerializer;
    }

    /**
     * Setter method for property <tt>valueSerializer</tt>.
     * @param valueSerializer value to be assigned to property valueSerializer
     * @author hewei
     */
    public void setValueSerializer(RedisSerializer valueSerializer) {
        this.valueSerializer = valueSerializer;
    }

    /**
     * Getter method for property <tt>connectionFactory</tt>.
     * @return property value of connectionFactory
     * @author hewei
     */
    public RedisConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

    /**
     * Getter method for property <tt>redisTemplate</tt>.
     * @return property value of redisTemplate
     * @author hewei
     */
    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    /**
     * 获取Redis服务器时间
     * @return
     */
    public Long time() {
        return (Long) redisTemplate.execute((RedisCallback<Long>) connection -> connection.time());
    }

    /**
     * @return
     * @see RedisTemplate#opsForList()
     */
    public ListOperations<String, MessageWrapper> opsForList() {
        return redisTemplate.opsForList();
    }

    /**
     * @param channel
     * @param message
     * @see RedisTemplate#convertAndSend(String, Object)
     */
    public void convertAndSend(String channel, Object message) {
        redisTemplate.convertAndSend(channel, message);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(getConnectionFactory(), "RedisConnectionFactory is required");
        Assert.notNull(getRedisTemplate(), "RedisTemplate is required");

        if (this.keySerializer == null) {
            this.keySerializer = new KeySerializer();
        }

        if (this.valueSerializer == null) {
            this.valueSerializer = new GenericJackson2JsonRedisSerializer();
        }

        // redisTemplate
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(keySerializer);
        redisTemplate.setValueSerializer(valueSerializer);
        redisTemplate.setHashKeySerializer(keySerializer);
        redisTemplate.setHashValueSerializer(valueSerializer);
        redisTemplate.afterPropertiesSet();
    }
}
