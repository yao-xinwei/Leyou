package com.java.auth.controller;

import com.java.auth.config.JwtProperties;
import com.java.auth.entity.UserInfo;
import com.java.auth.service.AuthService;
import com.java.auth.utils.JwtUtils;
import com.java.utils.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@EnableConfigurationProperties(JwtProperties.class)
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private JwtProperties jwtProperties;
    //登录权授
    @PostMapping("accredit")
    public ResponseEntity<Void> accredit(@RequestParam("username") String username,
                                         @RequestParam("password") String password,
                                         HttpServletRequest request,
                                         HttpServletResponse response) throws Exception {
        String token = authService.accredit(username,password);
        if (token == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        //向浏览器端写入cookie                        cookie的名字            加密后的字符串  cookie的生命周期                    字符集     cookie设置只读的
        CookieUtils.setCookie(request,response,jwtProperties.getCookieName(),token,jwtProperties.getCookieMaxAge(),null,true);
        return ResponseEntity.ok().build();
    }
    //前端显示登录的用户名
    @GetMapping("verify")//                 获取cookie的值，只需要传入cookie的名字
    public ResponseEntity<UserInfo> verify(@CookieValue("LY_TOKEN") String token,
                                           HttpServletRequest request,
                                           HttpServletResponse response){
        //解密                                 公钥
        try {
            UserInfo infoFromToken = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());
            if(infoFromToken != null){
                //刷新token里面的字符串，防止cookie过期
                //重新产生token
                String token1 = JwtUtils.generateToken(infoFromToken, jwtProperties.getPrivateKey(), jwtProperties.getExpire());
                //在写入写入cookie中
                //                                     cookie的名字            加密后的字符串  cookie的生命周期                    字符集     cookie设置只读的
                CookieUtils.setCookie(request,response,jwtProperties.getCookieName(),token,jwtProperties.getCookieMaxAge(),null,true);
                return ResponseEntity.ok(infoFromToken);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

}
