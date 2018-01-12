package com.itfsw.redis.mq.support.channel;

import com.itfsw.redis.mq.MessageChannel;
import com.itfsw.redis.mq.redis.RedisOperations;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * ---------------------------------------------------------------------------
 *
 * ---------------------------------------------------------------------------
 * @author: hewei
 * @time:2018/1/8 15:44
 * ---------------------------------------------------------------------------
 */
public class DefaultMessageChannel<T> implements MessageChannel<T> {
    public final static String KEY_FOR_CHANNEL_POOL = "channel-pools"; // channel 池的key
    public final static String KEY_FOR_CHANNEL_SUBS = "channel-subs"; // channel 订阅者
    private RedisOperations redisOps;   // redis 操作
    private String channelName; // 频道名称
    private ScheduledExecutorService channelPoolKeepScheduled;  // 心跳

    /**
     * 构造函数
     * @param redisOps
     * @param channelName
     */
    public DefaultMessageChannel(RedisOperations redisOps, String channelName) {
        this.redisOps = redisOps;
        this.channelName = channelName;
    }

    @Override
    public String getChannel() {
        return channelName;
    }

    @Override
    public RedisOperations redisOps() {
        return redisOps;
    }

    @Override
    public void destroy() throws Exception {
        // 注销信息
        if (channelPoolKeepScheduled != null && !channelPoolKeepScheduled.isShutdown()){
            redisOps.getRedisTemplate().opsForHash().delete(KEY_FOR_CHANNEL_POOL, channelName);
            channelPoolKeepScheduled.shutdown();
            channelPoolKeepScheduled = null;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 注册信息
        if (channelPoolKeepScheduled == null){
            channelPoolKeepScheduled = Executors.newSingleThreadScheduledExecutor();
            // 每分钟更新
            channelPoolKeepScheduled.scheduleAtFixedRate(() -> redisOps.getRedisTemplate().opsForHash().put(KEY_FOR_CHANNEL_POOL, channelName, redisOps.time()), 0, 1, TimeUnit.MINUTES);
        }
    }
}
