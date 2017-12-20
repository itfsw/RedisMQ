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

package com.itfsw.redis.mq.support.producer;

import com.itfsw.redis.mq.MessageProducer;
import com.itfsw.redis.mq.MessageQueue;
import com.itfsw.redis.mq.MessageSender;
import com.itfsw.redis.mq.model.MessageWrapper;
import com.itfsw.redis.mq.redis.RedisOperations;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.text.SimpleDateFormat;

/**
 * ---------------------------------------------------------------------------
 * 默认 MessageProducer 实现
 * ---------------------------------------------------------------------------
 * @author: hewei
 * @time:2017/11/17 13:55
 * ---------------------------------------------------------------------------
 */
public class DefaultMessageProducer<T> implements MessageProducer<T>, InitializingBean, DisposableBean {
    private MessageQueue<T> queue;  // 消息队列
    private IdWorker idWorker;  // id 生成器

    /**
     * 构造函数
     * @param messageQueue
     */
    public DefaultMessageProducer(MessageQueue<T> messageQueue) {
        this.queue = messageQueue;
    }

    @Override
    public void send(T message) {
        new DefaultMessageSender<>(message).send();
    }

    @Override
    public MessageSender<T> create(T message) {
        return new DefaultMessageSender<>(message);
    }

    @Override
    public MessageQueue<T> getQueue() {
        return queue;
    }

    @Override
    public void destroy() throws Exception {
        if (idWorker != null) {
            idWorker.unRegIdWorker();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (idWorker == null) {
            idWorker = new IdWorker(queue.redisOps());
            idWorker.regIdWorker();
        }
    }

    /**
     * 消息发送器
     * @param <T>
     */
    private class DefaultMessageSender<T> implements MessageSender<T> {
        private T message;  // 消息
        private long expires = -1L;   // 有效期
        private boolean highPriority = false;   // 高优先级

        /**
         * 构造函数
         * @param message
         */
        public DefaultMessageSender(T message) {
            this.message = message;
        }

        @Override
        public MessageSender<T> withExpires(long timeToLive) {
            this.expires = timeToLive;
            return this;
        }

        @Override
        public MessageSender<T> withHighPriority(boolean highPriority) {
            this.highPriority = highPriority;
            return this;
        }

        @Override
        public void send() {
            MessageWrapper<T> messageWrapper = new MessageWrapper<>();
            messageWrapper.setMessage(message);
            messageWrapper.setMessageId(idWorker.nextId());
            messageWrapper.setExpires(expires < 0 ? -1 : expires);
            messageWrapper.setHighPriority(highPriority);
            messageWrapper.setCreateTime(queue.redisOps().time());
            if (messageWrapper.getHighPriority()) {
                queue.preAdd(messageWrapper);
            } else {
                queue.add(messageWrapper);
            }
        }
    }

    /**
     * id生成器
     */
    private static class IdWorker {
        private RedisOperations redisOps;
        /**
         * 上次生成ID的时间截
         */
        private long lastTimestamp = -1L;
        /**
         * 毫秒内序列(0~4095)
         */
        private long sequence = 0L;
        /**
         * 序列在id中占的位数
         */
        private final long sequenceBits = 12L;
        /**
         * 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095)
         */
        private final long sequenceMask = -1L ^ (-1L << sequenceBits);
        private long workerId;

        /**
         * 构造函数
         * @param redisOps
         */
        public IdWorker(RedisOperations redisOps) {
            this.redisOps = redisOps;
        }

        public synchronized void regIdWorker() {
            this.workerId = redisOps.getRedisTemplate().opsForList().leftPush("id-workers", true);
        }

        public synchronized void unRegIdWorker() {
            redisOps.getRedisTemplate().opsForList().rightPop("id-workers");
        }

        /**
         * 获得下一个ID
         */
        public synchronized String nextId() {
            long timestamp = timeGen(); //获取当前毫秒数
            //如果服务器时间有问题(时钟后退) 报错。
            if (timestamp < lastTimestamp) {
                throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
            }
            //如果上次生成时间和当前时间相同,在同一毫秒内
            if (lastTimestamp == timestamp) {
                //sequence自增，因为sequence只有12bit，所以和sequenceMask相与一下，去掉高位
                sequence = (sequence + 1) & sequenceMask;
                //判断是否溢出,也就是每毫秒内超过4095，当为4096时，与sequenceMask相与，sequence就等于0
                if (sequence == 0) {
                    timestamp = tilNextMillis(lastTimestamp); //自旋等待到下一毫秒
                }
            } else {
                sequence = 0L; //如果和上次生成时间不同,重置sequence，就是下一毫秒开始，sequence计数重新从0开始累加
            }
            lastTimestamp = timestamp;


            String time = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(redisOps.time());

            return String.format("%s%04d%d", time, sequence, workerId);
        }

        /**
         * 阻塞到下一个毫秒，直到获得新的时间戳
         * @param lastTimestamp 上次生成ID的时间截
         * @return 当前时间戳
         */
        protected long tilNextMillis(long lastTimestamp) {
            long timestamp = timeGen();
            while (timestamp <= lastTimestamp) {
                timestamp = timeGen();
            }
            return timestamp;
        }

        /**
         * 返回以毫秒为单位的当前时间
         * @return 当前时间(毫秒)
         */
        protected long timeGen() {
            return System.currentTimeMillis();
        }
    }
}
