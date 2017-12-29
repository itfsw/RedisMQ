package com.itfsw.redis.mq.support.sender;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Random;

/**
 * ---------------------------------------------------------------------------
 *
 * ---------------------------------------------------------------------------
 * @author: hewei
 * @time:2017/12/28 18:42
 * ---------------------------------------------------------------------------
 */
public class IdWorker {
    /**
     * 上次生成ID的时间截
     */
    private static long lastTimestamp = -1L;
    /**
     * 毫秒内序列
     */
    private static long sequence = 0L;
    private static long workerId = 0L;

    static {
        long tmp = 0L;
        try {
            byte[] address = InetAddress.getLocalHost().getAddress();
            // 取前32位
            for (int i = 0; i < 4; i++) {
                tmp = (tmp | address[i]) << 8;
            }
        } catch (Throwable e) {
        } finally {
            workerId = Math.abs(tmp << 32 | new Random().nextInt());
        }
    }

    /**
     * 生成有序Id
     * @param millis
     * @return
     */
    public static synchronized String generateId(long millis) {
        long timestamp = millis; //获取当前毫秒数

        //如果服务器时间有问题(时钟后退) 报错。
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        //如果上次生成时间和当前时间相同,在同一毫秒内
        if (lastTimestamp == timestamp) {
            //sequence自增
            sequence++;
            //判断是否溢出
            if (sequence == Long.MAX_VALUE) {
                // 阻塞1毫秒(不可能进入该流程)
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(String.format("Thread InterruptedException.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp), e);
                }
                sequence = 0;
                timestamp++;
            }
        } else {
            sequence = 0L; //如果和上次生成时间不同,重置sequence，就是下一毫秒开始，sequence计数重新从0开始累加
        }
        lastTimestamp = timestamp;

        return String.format("%s-%s-%s", new SimpleDateFormat("yyyyMMddHHmmssSSS").format(lastTimestamp), Long.toString(sequence, Character.MAX_RADIX), Long.toString(workerId, Character.MAX_RADIX));
    }

    /**
     * 生成有序Id
     * @return
     */
    public static synchronized String nextId() {
        return generateId(System.currentTimeMillis());
    }
}
