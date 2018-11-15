package com.gramcha.services;

import com.gramcha.config.ApplicationConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Created by gramachandran on 13/11/18.
 */
@Service
public class RateLimitService {

    @Autowired
    private ApplicationConfiguration applicationConfiguration;

    @Autowired
    @Qualifier("BurstyRateLimiter")
    private RateLimiter burstyRateLimiter;

    @Autowired
    @Qualifier("UniformRateLimiter")
    private RateLimiter uniformRateLimiter;

    public boolean isWithinLimit(String policy){
        String type = applicationConfiguration.getRateLimitType();
        switch (type){
            case "burst":
                return burstyRateLimiter.isWithinLimit(policy);

            case "uniform":
                return  uniformRateLimiter.isWithinLimit(policy);
        }
        return false;
    }
}
