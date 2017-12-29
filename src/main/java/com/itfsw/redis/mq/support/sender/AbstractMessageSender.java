package com.itfsw.redis.mq.support.sender;

import com.itfsw.redis.mq.MessageSender;
import com.itfsw.redis.mq.model.MessageWrapper;
import com.itfsw.redis.mq.redis.RedisOperations;

/**
 * ---------------------------------------------------------------------------
 *
 * ---------------------------------------------------------------------------
 * @author: hewei
 * @time:2017/12/22 15:16
 * ---------------------------------------------------------------------------
 */
public abstract class AbstractMessageSender<T> implements MessageSender<T> {
    private RedisOperations redisOps;
    private T message;  // 消息
    private long expires = -1L;   // 有效期
    private boolean highPriority = false;   // 高优先级

    /**
     * 发送
     * @param messageWrapper
     */
    public abstract void send(MessageWrapper<T> messageWrapper);

    /**
     * 构造函数
     * @param redisOps
     */
    public AbstractMessageSender(RedisOperations redisOps) {
        this.redisOps = redisOps;
    }

    /**
     * message
     * @param message
     * @return
     */
    public MessageSender<T> withMessage(T message){
        this.message = message;
        return this;
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
    public String send() {
        MessageWrapper<T> messageWrapper = new MessageWrapper<>();
        long time = redisOps.time();

        String messageId = IdWorker.generateId(time);
        messageWrapper.setMessage(message);
        messageWrapper.setMessageId(messageId);
        messageWrapper.setExpires(expires < 0 ? -1 : expires);
        messageWrapper.setHighPriority(highPriority);
        messageWrapper.setCreateTime(time);
        send(messageWrapper);
        return messageId;
    }
}
