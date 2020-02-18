package com.java;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer //代表eureka的服务器端
public class LyRegistry {
    public static void main(String[] args) {
        SpringApplication.run(LyRegistry.class,args);
    }
}
