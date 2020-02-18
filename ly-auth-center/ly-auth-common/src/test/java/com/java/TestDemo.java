package com.java;

import com.java.auth.entity.UserInfo;
import com.java.auth.utils.JwtUtils;
import com.java.auth.utils.RsaUtils;
import org.junit.Before;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;

public class TestDemo {
    //产生公钥位置
    private static final String pubKeyPath = "D:\\tmp\\rsa.pub";
    //产生私钥位置
    private static final String priKeyPath = "D:\\tmp\\rsa.pri";

    private PublicKey publicKey;//公钥对象
    private PrivateKey privateKey;//私钥对象
    //产生公钥、私钥
   /* @Test
    public void init() throws Exception {
        //第三个参数是一个秘钥
        RsaUtils.generateKey(pubKeyPath,priKeyPath,"ty555");
    }*/
   //获取公钥和私钥
    @Before //在...之前运行
    public void loadData() throws Exception {
        //获取公钥
        publicKey = RsaUtils.getPublicKey(pubKeyPath);
        //获取私钥
        privateKey = RsaUtils.getPrivateKey(priKeyPath);
    }
    //产生token(加密后的字符串)
    @Test
    public void genToken() throws Exception {
        //                                           载荷                                私钥     产生字符串的寿命：分钟
        String token = JwtUtils.generateToken(new UserInfo(1L, "tom"), privateKey, 5);//加密后的字符串,写入cookie中
        System.out.println(token);
    }

    //进行解密（私钥加密,公钥解密）
    @Test
    public void jiemi() throws Exception {
        //String s = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MTIsInVzZXJuYW1lIjoidG9tIiwiZXhwIjoxNTc4ODMyNzY2fQ.H8uIUip8kG2i53P8NdVAAxeD1FMqc32ZM6QBLQNgTicIBW7UE_VKNPXtV0ZkQ3Wdmhp3xRWmeuAs87g5AirK8RZM_Gi1tP7NDnaG2ZkFn7S4c_VUdsjcmyB9Y7e3KY4d06yalgUxTl0nToT_2OqCX4BcW97XIWtVMIMIDDfYS3M";
        String s = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MSwidXNlcm5hbWUiOiJ0b20iLCJleHAiOjE1Nzg4MzMzMjZ9.fZneK47y06NUTK2TjGBEuc8f9YN6wTL6m95EQ0wo8Rr0LATDI-Tn8G3-YCrF_ui8fSBKD74nKOLc09FBNlsseIwzo7_aVZTj1QinAb7OB549tS25izL6_ymG15x5qWbPz2ihK_O80qHgB2rJqJ1ZwRAyRYTRIzCciXTFhArXh6I";
        UserInfo userinfo = JwtUtils.getInfoFromToken(s, publicKey);//载荷
        System.out.println(userinfo);
    }

}
