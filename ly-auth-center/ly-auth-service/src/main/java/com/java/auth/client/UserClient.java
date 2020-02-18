package com.java.auth.client;

import com.java.user.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
@FeignClient("user-service")//fegin客户端,调用其他服务
public interface UserClient {
    //查询用户名和密码
    @GetMapping("query")
    public User queryUser(@RequestParam("username") String username, @RequestParam("password") String password);

    }
