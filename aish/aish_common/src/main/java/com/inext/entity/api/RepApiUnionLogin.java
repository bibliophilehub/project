package com.inext.entity.api;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/4 0004.
 */
public class RepApiUnionLogin implements Serializable {
    /**
     * @Description: TODO
     */
    private static final long serialVersionUID = 1L;

    private String uuid;
    private String desKey;
    private String bizType;
    private String sourceChannel;
    private String targetChannel;
    private String sign;
    private String code;
    private String message;
    private String registerTime;
    private String black;
    private String userFlag;
    private String redirectUrl;


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDesKey() {
        return desKey;
    }

    public void setDesKey(String desKey) {
        this.desKey = desKey;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getSourceChannel() {
        return sourceChannel;
    }

    public void setSourceChannel(String sourceChannel) {
        this.sourceChannel = sourceChannel;
    }

    public String getTargetChannel() {
        return targetChannel;
    }

    public void setTargetChannel(String targetChannel) {
        this.targetChannel = targetChannel;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public String getBlack() {
        return black;
    }

    public void setBlack(String black) {
        this.black = black;
    }

    public String getUserFlag() {
        return userFlag;
    }

    public void setUserFlag(String userFlag) {
        this.userFlag = userFlag;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

}
