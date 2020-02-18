package com.java;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication//Springboot应用
@EnableDiscoveryClient//eureka客户端
@EnableFeignClients//开启feign
public class LyCartApplication {
    public static void main(String[] args) {
        SpringApplication.run(LyCartApplication.class, args);
    }
}