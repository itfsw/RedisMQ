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

package com.itfsw.redis.mq.utils;

import com.itfsw.redis.mq.MessageQueue;
import com.itfsw.redis.mq.redis.RedisOperations;
import com.itfsw.redis.mq.support.queue.DefaultMessageQueue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * ---------------------------------------------------------------------------
 *
 * ---------------------------------------------------------------------------
 * @author: hewei
 * @time:2017/11/10 17:14
 * ---------------------------------------------------------------------------
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class SpringTest {

    @Configuration
    @PropertySource(value = "classpath:redis.properties")
    public static class Config {

        @Bean
        public RedisConnectionFactory redisConnectionFactory(@Value("${host}") String host, @Value("${port}") int port) {
            JedisConnectionFactory factory = new JedisConnectionFactory();
            factory.setHostName(host);
            factory.setPort(port);
            factory.afterPropertiesSet();
            return factory;
        }

        @Bean
        public RedisOperations redisOperations(@Autowired RedisConnectionFactory connectionFactory){
            return new RedisOperations(connectionFactory);
        }

        @Bean
        public MessageQueue messageQueue(@Autowired RedisOperations redisOperations){
            return new DefaultMessageQueue(redisOperations);
        }
    }

    @Test
    public void test(){

    }
}
