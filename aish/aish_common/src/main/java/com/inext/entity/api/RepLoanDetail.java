package com.inext.entity.api;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2017/7/4 0004.
 */
public class RepLoanDetail implements Serializable {
    /**
     * @Description:
     */
    private static final long serialVersionUID = 1L;

    private String mobile;
    private String registerTime;
    private String userFlag;
    private String black;
    private String transNo;
    private String amount;
    private String borProgress;
    private String timeLimit;
    private Long merchantId;
    private Long merchantProductId;
    private Date createTime;


    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Long getMerchantProductId() {
        return merchantProductId;
    }

    public void setMerchantProductId(Long merchantProductId) {
        this.merchantProductId = merchantProductId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public String getUserFlag() {
        return userFlag;
    }

    public void setUserFlag(String userFlag) {
        this.userFlag = userFlag;
    }

    public String getBlack() {
        return black;
    }

    public void setBlack(String black) {
        this.black = black;
    }

    public String getTransNo() {
        return transNo;
    }

    public void setTransNo(String transNo) {
        this.transNo = transNo;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBorProgress() {
        return borProgress;
    }

    public void setBorProgress(String borProgress) {
        this.borProgress = borProgress;
    }

    public String getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(String timeLimit) {
        this.timeLimit = timeLimit;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "RepLoanDetail [mobile=" + mobile + ", registerTime=" + registerTime + ", userFlag=" + userFlag
                + ", black=" + black + ", transNo=" + transNo + ", amount=" + amount + ", borProgress=" + borProgress
                + ", timeLimit=" + timeLimit + ", merchantId=" + merchantId + ", merchantProductId=" + merchantProductId
                + ", createTime=" + createTime + "]";
    }


}
