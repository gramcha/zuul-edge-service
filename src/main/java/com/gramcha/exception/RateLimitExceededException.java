package com.gramcha.exception;

import com.netflix.zuul.exception.ZuulException;
import org.springframework.cloud.netflix.zuul.util.ZuulRuntimeException;
import org.springframework.http.HttpStatus;

/**
 * Created by gramachandran on 15/11/18.
 */
public class RateLimitExceededException extends ZuulRuntimeException {
    public RateLimitExceededException() {
        super(new ZuulException(HttpStatus.TOO_MANY_REQUESTS.toString(), HttpStatus.TOO_MANY_REQUESTS.value(), null));
    }
}
