package com.java;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients //开启feign
@EnableDiscoveryClient ////和EnableEurekaClient类似，单EnableEurekaClient较单一 eureka客户端 多种注册
@SpringBootApplication //SpringBoot应用
public class LyGodosPage {
    public static void main(String[] args) {
        SpringApplication.run(LyGodosPage.class,args);
    }
}
