package com.java.config;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "ly.pay")
public class PayProperties {
    //    appId: wx8397f8696b538317
    //    mchId: 1473426802
    //    key: 4T6m9iK73b0kn9g5v426MKfHQH7X8rKwb
    //    connectTimeoutMs: 5000
    //    readTimeoutMs: 10000

    private String appId; // 公众账号ID

    private String mchId; // 商户号

    private String key; // 生成签名的密钥

    private int connectTimeoutMs; // 连接超时时间

    private int readTimeoutMs;// 读取超时时间

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getConnectTimeoutMs() {
        return connectTimeoutMs;
    }

    public void setConnectTimeoutMs(int connectTimeoutMs) {
        this.connectTimeoutMs = connectTimeoutMs;
    }

    public int getReadTimeoutMs() {
        return readTimeoutMs;
    }

    public void setReadTimeoutMs(int readTimeoutMs) {
        this.readTimeoutMs = readTimeoutMs;
    }
}
