package com.java.auth.service;

import com.java.auth.client.UserClient;
import com.java.auth.config.JwtProperties;
import com.java.auth.entity.UserInfo;
import com.java.auth.utils.JwtUtils;
import com.java.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@EnableConfigurationProperties(JwtProperties.class)
public class AuthService {
    @Autowired
    private UserClient userClient;
    @Autowired
    private JwtProperties jwtProperties;
    //登录权授
    public String accredit(String username, String password){
        try {
            //使用feign调用用户微服务接口
            User user = userClient.queryUser(username, password);//查不到会报错
            if(user == null){
                return null;
            }
            //产生token（加密后的）
            String token = JwtUtils.generateToken(new UserInfo(user.getId(),user.getUsername()), jwtProperties.getPrivateKey(), jwtProperties.getExpire());//加密后的字符串,写入cookie中
            return token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
