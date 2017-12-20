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

package com.itfsw.redis.mq.model;

/**
 * ---------------------------------------------------------------------------
 * 消息包装器
 * ---------------------------------------------------------------------------
 * @author: hewei
 * @time:2017/11/15 15:49
 * ---------------------------------------------------------------------------
 */
public class MessageWrapper<T> {
    private String messageId;   // 消息ID
    private Long createTime;    // 创建日期
    private Long excuteTime;    // 执行时间
    private Long expires;   // 有效期
    private Boolean highPriority;   // 高优先级
    private T message; // 消息

    /**
     * Getter method for property <tt>messageId</tt>.
     * @return property value of messageId
     * @author hewei
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * Setter method for property <tt>messageId</tt>.
     * @param messageId value to be assigned to property messageId
     * @author hewei
     */
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    /**
     * Getter method for property <tt>createTime</tt>.
     * @return property value of createTime
     * @author hewei
     */
    public Long getCreateTime() {
        return createTime;
    }

    /**
     * Setter method for property <tt>createTime</tt>.
     * @param createTime value to be assigned to property createTime
     * @author hewei
     */
    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    /**
     * Getter method for property <tt>expires</tt>.
     * @return property value of expires
     * @author hewei
     */
    public Long getExpires() {
        return expires;
    }

    /**
     * Setter method for property <tt>expires</tt>.
     * @param expires value to be assigned to property expires
     * @author hewei
     */
    public void setExpires(Long expires) {
        this.expires = expires;
    }

    /**
     * Getter method for property <tt>highPriority</tt>.
     * @return property value of highPriority
     * @author hewei
     */
    public Boolean getHighPriority() {
        return highPriority;
    }

    /**
     * Setter method for property <tt>highPriority</tt>.
     * @param highPriority value to be assigned to property highPriority
     * @author hewei
     */
    public void setHighPriority(Boolean highPriority) {
        this.highPriority = highPriority;
    }

    /**
     * Getter method for property <tt>message</tt>.
     * @return property value of message
     * @author hewei
     */
    public T getMessage() {
        return message;
    }

    /**
     * Setter method for property <tt>message</tt>.
     * @param message value to be assigned to property message
     * @author hewei
     */
    public void setMessage(T message) {
        this.message = message;
    }

    /**
     * Getter method for property <tt>excuteTime</tt>.
     * @return property value of excuteTime
     * @author hewei
     */
    public Long getExcuteTime() {
        return excuteTime;
    }

    /**
     * Setter method for property <tt>excuteTime</tt>.
     * @param excuteTime value to be assigned to property excuteTime
     * @author hewei
     */
    public void setExcuteTime(Long excuteTime) {
        this.excuteTime = excuteTime;
    }
}
