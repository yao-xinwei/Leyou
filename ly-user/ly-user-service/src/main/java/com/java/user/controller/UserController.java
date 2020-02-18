package com.java.user.controller;

import com.java.user.pojo.User;
import com.java.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class UserController {
    @Autowired
    private UserService userService;
    //用户数据的校验，主要包括对：手机号、用户名的唯一性校验
    @GetMapping("/check/{data}/{type}") // data要校验的数据，type要校验的数据类型：1，用户名；2，手机
    public ResponseEntity<Boolean> check(@PathVariable("data") String data,@PathVariable("type") Integer type){
        Boolean bol = userService.check(data,type);
        if(bol == false){
            //不可用
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(bol);
    }
    //发送短信功能
    @PostMapping("code")
    public ResponseEntity<Void> sendVertfyCode(@RequestParam("phone") String phone){
        Boolean bol = userService.sendVertfyCode(phone);
        if(bol != null && bol){//bol 为true
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    //注册用户信息
    @PostMapping("register")//     要校验的参数上添加@Valid注解
    public ResponseEntity<Void> createUser(@Valid User user, @RequestParam("code") String code){
        Boolean b=userService.createUser(user,code);
        if(null!=b&&b){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    //查询用户名和密码
    @GetMapping("query")
    public ResponseEntity<User> queryUser(@RequestParam("username") String username,@RequestParam("password") String password){
       User user = userService.queryUser(username,password);
       if(user != null){
           return ResponseEntity.ok(user);
       }
       return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

}
