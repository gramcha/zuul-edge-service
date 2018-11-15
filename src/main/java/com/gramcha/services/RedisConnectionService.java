package com.gramcha.services;

import com.gramcha.config.ApplicationConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.annotation.PostConstruct;

/**
 * Created by gramachandran on 13/11/18.
 */
@Service
public class RedisConnectionService {

    Jedis redisConnection = null;

    @Autowired
    ApplicationConfiguration applicationConfiguration;

    void createConnection(){
        redisConnection = new Jedis(applicationConfiguration.getRedisHost(),applicationConfiguration.getRedisPort());
    }
    @PostConstruct
    void init(){
        if(redisConnection==null){
            createConnection();
        }
    }

    public Jedis getRedisConnection() {
        if(redisConnection==null){
            createConnection();
        }
        return redisConnection;
    }
}
