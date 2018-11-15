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
@Qualifier("BurstyRateLimiter")
public class BurstyRateLimiter implements RateLimiter {
    @Autowired
    ApplicationConfiguration applicationConfiguration;
    @Autowired
    RedisConnectionService redis;

    final String TYPE = "Bursty_";
    @Override
    public boolean isWithinLimit(String policy){
        Jedis jedis = redis.getRedisConnection();
        String key = TYPE+policy;
        long currentTime = System.currentTimeMillis();
        long lastWindow = (currentTime - (applicationConfiguration.getRateLimitTimeWindow() * 1000))/1000;

        //delete all messages outside the window
        jedis.zremrangeByScore(key, "0", Long.toString(lastWindow));

        //is zcard less than the allowed number of actions
        long card = jedis.zcard(key);

        System.out.println("count = "+ card);
        //If yes, add message to the sorted set and return true
        if(card < applicationConfiguration.getRateLimitCount()){
            jedis.zadd(key, (currentTime/1000), policy);
            System.out.println("withinlimit = true");
            return true;
        }
        System.out.println("withinlimit = false");
        return false;
    }
}
