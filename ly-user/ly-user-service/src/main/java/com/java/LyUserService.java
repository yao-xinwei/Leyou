package com.java;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.java.user.mapper") //扫描mapper，将下面的接口变成一个个实体对象
public class LyUserService {
    public static void main(String[] args) {
        SpringApplication.run(LyUserService.class,args);
    }
}