package com.inext.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by lisige on 17/1/10. 自定义属性配置
 */
@Configuration("properties")
@ConfigurationProperties(prefix = "clSms")
@EnableConfigurationProperties(SmsProperties.class)
public class SmsProperties {

    private String appKey;
    private String nonceStr;
    private String appSecret;
    private String smsSendUrl;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getSmsSendUrl() {
        return smsSendUrl;
    }

    public void setSmsSendUrl(String smsSendUrl) {
        this.smsSendUrl = smsSendUrl;
    }
}
