package com.java;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication //SpringBoot应用
@EnableDiscoveryClient //和EnableEurekaClient类似，单EnableEurekaClient较单一 eureka客户端 多种注册
@EnableFeignClients //开启feign
public class LySearchService {
    public static void main(String[] args) {
        SpringApplication.run(LySearchService.class,args);
    }
}
