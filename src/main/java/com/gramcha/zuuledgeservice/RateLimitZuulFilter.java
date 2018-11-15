package com.gramcha.zuuledgeservice;

import com.gramcha.config.ApplicationConfiguration;
import com.gramcha.exception.RateLimitExceededException;
import com.gramcha.services.RateLimitService;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * Created by gramachandran on 12/11/18.
 */
public class RateLimitZuulFilter extends ZuulFilter {

    @Autowired
    ApplicationConfiguration applicationConfiguration;

    @Autowired
    RateLimitService rateLimitService;

    //https://github.com/spring-cloud-samples/sample-zuul-filters/tree/master/src/main/java/org/springframework/cloud/samplezuulfilters
    //implement the logic for rate limit here
    public String filterType() {
        return "pre";
    }

    public int filterOrder() {
        return 999;
    }

    public boolean shouldFilter() {
        return true;
    }

    public Object run() {
        if(applicationConfiguration.isRateLimitEnabled()){
            System.out.println("addrespfilter");
            RequestContext context = RequestContext.getCurrentContext();
            Object userNameAttr = context.getRequest().getAttribute("authUser");
            if(userNameAttr!=null){
                String userName = userNameAttr.toString();
                System.out.println("addrespfilter userName = "+userName);
                HttpServletResponse servletResponse = context.getResponse();

                if(rateLimitService.isWithinLimit(userName)){
                    //no need to change response since it is within limit.
                    servletResponse.addHeader("Within-Window", "true");
                } else {
                    servletResponse.setStatus(429);//Too many requests
                    servletResponse.addHeader("Retry-After", applicationConfiguration.getRateLimitTimeWindow().toString()+" seconds");
                    servletResponse.addHeader("Within-Window", "false");
                    throw new RateLimitExceededException();
                }
            }
        }

        return null;
    }
}
