package com.java;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy //开启zuul
@EnableEurekaClient //eureka客户端
public class LyApiGateway {
    public static void main(String[] args) {
        SpringApplication.run(LyApiGateway.class,args);
    }
}
