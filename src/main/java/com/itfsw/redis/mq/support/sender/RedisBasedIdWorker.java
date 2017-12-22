package com.itfsw.redis.mq.support.sender;

import com.itfsw.redis.mq.redis.RedisOperations;

import java.text.SimpleDateFormat;

/**
 * ---------------------------------------------------------------------------
 *
 * ---------------------------------------------------------------------------
 * @author: hewei
 * @time:2017/12/22 15:17
 * ---------------------------------------------------------------------------
 */
public class RedisBasedIdWorker {
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
     * 私有构造函数
     */
    private RedisBasedIdWorker(){}

    /**
     * 注册id worker
     * @param redisOps
     * @return
     */
    public synchronized static RedisBasedIdWorker regIdWorker(RedisOperations redisOps) {
        RedisBasedIdWorker worker = new RedisBasedIdWorker();
        worker.workerId = redisOps.getRedisTemplate().opsForList().leftPush("id-workers", true);
        return worker;
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


        String time = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(time());

        return String.format("%s%04d%d", time, sequence, workerId);
    }

    /**
     * 获取当前毫秒数
     * @return
     */
    public long time(){
        return redisOps.time();
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
