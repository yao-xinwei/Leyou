package com.java.interceptors;


import com.java.auth.entity.UserInfo;
import com.java.auth.utils.JwtUtils;
import com.java.config.JwtProperties;
import com.java.utils.CookieUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor extends HandlerInterceptorAdapter {

    private JwtProperties jwtProperties;
    // 定义一个线程域，存放登录用户
    private static final ThreadLocal<UserInfo> s = new ThreadLocal();

    public LoginInterceptor(JwtProperties jwtProperties) {
       this.jwtProperties = jwtProperties;
    }

    @Override //在之前拦截
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //从cookie中获取值
        //从request中拿出cookie
        String cookieValue = CookieUtils.getCookieValue(request, jwtProperties.getCookieName());
        if(StringUtils.isBlank(cookieValue)){//如果cookie为空
            // 抛出异常，证明未登录,返回401
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        try{//如果cookie不为空
            //取出后进行解密
            UserInfo infoFromToken = JwtUtils.getInfoFromToken(cookieValue, jwtProperties.getPublicKey());
            //放入线程
            s.set(infoFromToken);
            return true;
        }catch (Exception e){
            //解密失败（能去到cookie）
            // 抛出异常，证明未登录,返回401
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }

    }

    @Override //在之后拦截
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //移除线程
        s.remove();
    }
    public static UserInfo getUserInfo(){
        return s.get();
    }
}
