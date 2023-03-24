package com.alpgo.common.message;

import com.alpgo.common.core.redis.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisPublisher {
    @Autowired
    private RedisCache redisCache;

    public void publish(String channel, Object message) {
        redisCache.convertAndSend(channel, message);
    }
}
