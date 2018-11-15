package com.gramcha.config;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import sun.java2d.pipe.SolidTextRenderer;

import javax.print.DocFlavor;

/**
 * Created by gramachandran on 26/10/18.
 */
@Configuration
public class ApplicationConfiguration {
    @Autowired
    private Environment environment;

    public String getRedisHost() {
        return environment.getProperty("spring.redis.host");
    }

    public Integer getRedisPort() {
        return environment.getProperty("spring.redis.port", Integer.class);
    }

    public boolean isRateLimitEnabled() {
        return environment.getProperty("rateLimit.enabled", Boolean.class);
    }

    public String getRateLimitPolicy() {
        return environment.getProperty("rateLimit.policy");
    }

    public String getRateLimitType() {
        return environment.getProperty("rateLimit.type");
    }

    public Integer getRateLimitTimeWindow() {
        return environment.getProperty("rateLimit.timeWindow", Integer.class);
    }

    public Integer getRateLimitCount() {
        return environment.getProperty("rateLimit.requestCount", Integer.class);
    }
}
