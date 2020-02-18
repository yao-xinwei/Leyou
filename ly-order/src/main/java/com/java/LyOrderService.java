package com.java;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient //erueka客户端
@EnableFeignClients //开启feign
public class LyOrderService {
    public static void main(String[] args) {
        SpringApplication.run(LyOrderService.class,args);
    }
}
