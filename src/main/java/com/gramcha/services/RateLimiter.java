package com.gramcha.services;

/**
 * Created by gramachandran on 13/11/18.
 */
public interface RateLimiter {
    boolean isWithinLimit(String policy);
}
