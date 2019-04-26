package com.inext.entity.api;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/7/4 0004.
 */
public class RepLoan implements Serializable {
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
    private List<RepLoanDetail> bizData;

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

    public List<RepLoanDetail> getBizData() {
        return bizData;
    }

    public void setBizData(List<RepLoanDetail> bizData) {
        this.bizData = bizData;
    }


}
