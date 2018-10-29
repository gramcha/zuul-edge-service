package com.gramcha.zuuledgeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
@EnableEurekaClient     // Register this service as uerka client to eureka server
@EnableZuulProxy        // Enable Zuul
@ComponentScan(basePackages = "com.gramcha.*")
public class ZuulEdgeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZuulEdgeServiceApplication.class, args);
	}
}
