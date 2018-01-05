package com.itfsw.redis.mq.support.sender;

import java.net.InetAddress;
import java.net.NetworkInterface;
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
    private static long lastTimestamp = -1L;    // 上次生成的时间戳
    private static short sequence = 0;  // 毫秒内序列
    private static long mac = 0L;   // 机器标识
    private static short fix = 0;   // 修正位

    static {
        // 获取机器标识
        try {
            byte[] macBytes = NetworkInterface.getByInetAddress(InetAddress.getLocalHost()).getHardwareAddress();
            for (byte bt : macBytes) {
                mac = mac << 8 | bt;
            }
        } catch (Throwable e) {
            mac = new Random().nextLong() >>> 16;
        }
        // 修正位
        fix = (short) (Math.abs(new Random().nextInt(Short.MAX_VALUE)));
    }

    /**
     * 生成有序Id（最大32位）
     * @param millis
     * @return
     */
    public static synchronized String generateId(long millis) {
        long timestamp = millis; //获取当前毫秒数

        //如果服务器时间有问题(时钟后退) 报错。
        if (timestamp < lastTimestamp) {
            fixed();
        }

        //如果上次生成时间和当前时间相同,在同一毫秒内
        if (lastTimestamp == timestamp) {
            //sequence自增
            sequence++;
            //判断是否溢出
            if (sequence == Short.MAX_VALUE) {
                // 阻塞1毫秒(不可能进入该流程)
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    fixed();
                }
                sequence = 0;
                timestamp++;
            }
        } else {
            sequence = 0; //如果和上次生成时间不同,重置sequence，就是下一毫秒开始，sequence计数重新从0开始累加
        }
        lastTimestamp = timestamp;

        return String.format("%s-%s-%s-%s",
                Long.toString(lastTimestamp, Character.MAX_RADIX),
                Long.toString(sequence, Character.MAX_RADIX),
                Long.toString(mac, Character.MAX_RADIX),
                Long.toString(fix, Character.MAX_RADIX)
        );
    }

    /**
     * 修正
     */
    private static void fixed() {
        if (fix++ == Short.MAX_VALUE) {
            fix = 0;
        }
    }

    /**
     * 生成有序Id
     * @return
     */
    public static synchronized String nextId() {
        return generateId(System.currentTimeMillis());
    }
}
