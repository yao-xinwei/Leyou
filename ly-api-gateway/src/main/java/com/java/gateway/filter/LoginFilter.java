package com.java.gateway.filter;

import com.java.auth.entity.UserInfo;
import com.java.auth.utils.JwtUtils;
import com.java.gateway.config.FilterProperties;
import com.java.gateway.config.JwtProperties;
import com.java.utils.CookieUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component
@EnableConfigurationProperties({JwtProperties.class, FilterProperties.class})
public class LoginFilter extends ZuulFilter {
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private FilterProperties filterProperties;
    //过滤的类型
    @Override
    public String filterType() {
        //转发一个服务之前校验
        return FilterConstants.PRE_TYPE;
    }
    //在请求头之前查看请求参数
    @Override
    public int filterOrder() {
        return FilterConstants.PRE_DECORATION_FILTER_ORDER-1;
    }

    @Override
    public boolean shouldFilter() {
        //获取请求的上下文
        RequestContext currentContext = RequestContext.getCurrentContext();
        //获取Request请求
        HttpServletRequest request = currentContext.getRequest();
        //请求的路径
        String requestURI = request.getRequestURI();
        //不需要过滤的(配置文件的白名单)
        List<String> allowPaths = filterProperties.getAllowPaths();
        /*allowPaths.forEach(t->{
            if(requestURI.startsWith(t)){//判断请求路径中包含的白名单中的请求路径，不过滤
                return false;
            }
        });*/
       for(String url:allowPaths){
           if(requestURI.startsWith(url)){//迭代白名单数组，如果请求的uri以白名单中某一项开头，则过滤器不生效
               return false;
           }
       }
        //需要过滤
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        //获取cookie
        //获取请求的上下文
        RequestContext currentContext = RequestContext.getCurrentContext();
        //获取Request请求
        HttpServletRequest request = currentContext.getRequest();
        try {
            //从request中拿出cookie
            String cookieValue = CookieUtils.getCookieValue(request, jwtProperties.getCookieName());
            //取出后进行解密
            UserInfo infoFromToken = JwtUtils.getInfoFromToken(cookieValue, jwtProperties.getPublicKey());
        } catch (Exception e) {
            //可能没有登录和用户登录token是假的
            //不放过，返回一个响应码
            currentContext.setSendZuulResponse(false);
            currentContext.setResponseStatusCode(403);
    }
        return null;
    }
}
