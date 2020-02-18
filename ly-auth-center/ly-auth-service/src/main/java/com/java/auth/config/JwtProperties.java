package com.java.auth.config;

import com.java.auth.utils.RsaUtils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * 加载jwt配置文件
 */
@Data
@ConfigurationProperties(prefix = "ly.jwt")
public class JwtProperties {
    /* secret: ly@Login(Auth}*^31)&hei% # 登录校验的密钥
    pubKeyPath: D:/tmp/rsa/rsa.pub # 公钥地址
    priKeyPath: D:/tmp/rsa/rsa.pri # 私钥地址
    expire: 30 # token的过期时间,单位分钟
    cookieName: LY_TOKEN # 指定cookie名字，找加密后的token
    cookieMaxAge: 1800 # 存活时间*/
    private String secret;
    private String pubKeyPath;
    private String priKeyPath;
    private Integer expire;
    private String cookieName;
    private Integer cookieMaxAge;
    private PublicKey publicKey;//公钥对象
    private PrivateKey privateKey;//私钥对象
    //产生公钥和私钥
    @PostConstruct//类似构造方法
    public void init() throws Exception {
        //创建文件
        File file = new File(pubKeyPath);//公钥路径
        File file1 = new File(priKeyPath);//私钥路径
        if(!file.exists() && !file1.exists()){//没有公钥和私钥
            //产生公钥和私钥                              秘钥
            RsaUtils.generateKey(pubKeyPath,priKeyPath,secret);
        }
        //获取公钥和私钥
        //获取公钥
        publicKey = RsaUtils.getPublicKey(pubKeyPath);
        //获取私钥
        privateKey = RsaUtils.getPrivateKey(priKeyPath);

    }
}
