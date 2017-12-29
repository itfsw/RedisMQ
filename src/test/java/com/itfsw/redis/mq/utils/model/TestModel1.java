package com.itfsw.redis.mq.utils.model;

/**
 * ---------------------------------------------------------------------------
 *
 * ---------------------------------------------------------------------------
 * @author: hewei
 * @time:2017/12/28 18:20
 * ---------------------------------------------------------------------------
 */
public class TestModel1 {
    private String message;

    public TestModel1(String message) {
        this.message = message;
    }

    /**
     * Getter method for property <tt>message</tt>.
     * @return property value of message
     * @author hewei
     */
    public String getMessage() {
        return message;
    }

    /**
     * Setter method for property <tt>message</tt>.
     * @param message value to be assigned to property message
     * @author hewei
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
