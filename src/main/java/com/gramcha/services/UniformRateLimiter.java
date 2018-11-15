package com.gramcha.services;

import com.gramcha.config.ApplicationConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

/**
 * Created by gramachandran on 13/11/18.
 */
@Service
@Qualifier("UniformRateLimiter")
public class UniformRateLimiter implements RateLimiter {
    @Autowired
    ApplicationConfiguration applicationConfiguration;
    @Autowired
    RedisConnectionService redis;

    final String TYPE = "Uniform_";
    @Override
    public boolean isWithinLimit(String policy) {
        Jedis jedis = redis.getRedisConnection();
        String key = TYPE+policy;
        // check if the last message exists.
        long ttl = jedis.ttl(key);
        if(ttl > 0){
            return false;
        }

        // The key lives through the period defined by the interval
        if(key != null && policy != null){
            int interval = applicationConfiguration.getRateLimitTimeWindow()/applicationConfiguration.getRateLimitCount();
            jedis.setex(key, interval, policy);
            return true;
        }
        return false;
    }
}
