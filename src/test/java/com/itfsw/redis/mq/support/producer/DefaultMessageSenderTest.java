package com.itfsw.redis.mq.support.producer;

import com.itfsw.redis.mq.MessageQueue;
import com.itfsw.redis.mq.redis.RedisOperations;
import com.itfsw.redis.mq.support.sender.IdWorker;
import com.itfsw.redis.mq.utils.SpringTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * ---------------------------------------------------------------------------
 *
 * ---------------------------------------------------------------------------
 * @author: hewei
 * @time:2017/12/28 18:07
 * ---------------------------------------------------------------------------
 */
public class DefaultMessageSenderTest extends SpringTest {
    @Autowired
    private MessageQueue queue;

    @Test
    public void send() throws Exception {
        long time = new Date().getTime();
       RedisOperations redisOperations = queue.redisOps();
        for (int i = 0; i < 1000000; i ++){
           IdWorker.generateId(redisOperations.time());
        }

        System.out.println(new Date().getTime() - time);
    }
}