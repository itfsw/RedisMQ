package com.itfsw.redis.mq.support.producer;

import com.itfsw.redis.mq.MessageQueue;
import com.itfsw.redis.mq.utils.SpringTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

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

    }
}