package com.itfsw.redis.mq.support.sender;

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
     * 毫秒内序列(0~262143)
     */
    private static long sequence = 0L;
    /**
     * 序列在id中占的位数
     */
    private static final long sequenceBits = 18L;
    /**
     * 生成序列的掩码，这里为262143
     */
    private static final long sequenceMask = -1L ^ (-1L << sequenceBits);
    private static long workerId = Math.abs((long) (new Random().nextInt()) << sequenceBits);  // worker id (冲突概率 2^31 也就是 1/2147483648)

    /**
     * 生成Id(32位字符串)
     * @param time
     * @return
     */
    public static synchronized String generateId(long time) {
        long timestamp = time; //获取当前毫秒数
        //如果上次生成时间和当前时间相同,在同一毫秒内
        if (lastTimestamp == timestamp) {
            //sequence自增，因为sequence只有16bit，所以和sequenceMask相与一下，去掉高位
            sequence = (sequence + 1) & sequenceMask;
            //判断是否溢出,也就是每毫秒内超过262143，当为262143时，与sequenceMask相与，sequence就等于0，几乎不可能
            if (sequence == 0) {
                // 阻塞1毫秒
                sleepToNextMillis();
                timestamp++;
            }
        } else {
            sequence = 0L; //如果和上次生成时间不同,重置sequence，就是下一毫秒开始，sequence计数重新从0开始累加
        }
        lastTimestamp = timestamp;

        return String.format("%s%d", new SimpleDateFormat("yyyyMMddHHmmssSSS").format(lastTimestamp), workerId | sequence);
    }

    /**
     * 阻塞一毫秒
     */
    private static void sleepToNextMillis() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            long timestamp = System.currentTimeMillis();
            while (timestamp == System.currentTimeMillis()) {
            }
        }
    }
}
