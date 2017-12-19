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

package com.itfsw.redis.mq.redis.serializer;

import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.nio.charset.Charset;

/**
 * ---------------------------------------------------------------------------
 *
 * ---------------------------------------------------------------------------
 * @author: hewei
 * @time:2017/11/8 16:16
 * ---------------------------------------------------------------------------
 */
public class KeySerializer extends StringRedisSerializer {
    private final String prefix;

    public KeySerializer() {
        super();
        this.prefix = "mq:";
    }

    public KeySerializer(String prefix) {
        super();
        this.prefix = prefix;
    }

    public KeySerializer(String prefix, Charset charset) {
        super(charset);
        this.prefix = prefix;
    }

    public byte[] serialize(String string) throws SerializationException {
        return (string == null ? null : new StringBuffer(prefix).append(string).toString().getBytes());
    }

    public String deserialize(byte[] bytes) throws SerializationException {
        return (bytes == null ? null : new StringBuffer(new String(bytes)).deleteCharAt(prefix.length()).toString());
    }
}
