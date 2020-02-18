package com.java.config;

import com.java.auth.utils.RsaUtils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.security.PublicKey;

/**
 * 加载jwt配置文件
 */
@Data
@ConfigurationProperties(prefix = "ly.jwt")
public class JwtProperties {
    /*
    pubKeyPath: D:/tmp/rsa/rsa.pub # 公钥地址
    cookieName: LY_TOKEN # 指定cookie名字，找加密后的token*/
    private String pubKeyPath;
    private String cookieName;
    private PublicKey publicKey;//公钥对象
    //获取公钥
    @PostConstruct//类似构造方法
    public void init() throws Exception {
        //获取公钥
        publicKey = RsaUtils.getPublicKey(pubKeyPath);
    }
}
