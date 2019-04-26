package com.inext.dto;

import com.inext.exception.CheckException;
import com.inext.utils.DateUtil;

import javax.servlet.http.HttpServletRequest;

public class UnionAutoDataDto {
    public final static String MMPUSERKEY = "unionAuto:edit:";

    private String merchantId;//商戶id
    private String merchantProductId;//商品id
    private String userPhone;//用戶手机号码
    private String isOld;//是否是老用户
    private String isBlack;//是否是黑名单
    private String pushChannelId;//发布渠道
    private String sourceChannelId;//来源渠道
    private String days;//日期

    public UnionAutoDataDto() {
    }

    public UnionAutoDataDto(HttpServletRequest request) {
        this.setIsBlack(request.getParameter("isBlack"));
        this.setMerchantId(request.getParameter("merchantId"));
        this.setMerchantProductId(request.getParameter("merchantProductId"));
        this.setIsOld(request.getParameter("isOld"));
        this.setUserPhone(request.getParameter("userPhone"));
        this.setPushChannelId(request.getParameter("pushChannelId"));
        this.setSourceChannelId(request.getParameter("sourceChannelId"));
        this.setDays(DateUtil.getDateFormat("yyyy-MM-dd"));
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantProductId() {
        return merchantProductId;
    }

    public void setMerchantProductId(String merchantProductId) {
        this.merchantProductId = merchantProductId;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getIsOld() {
        return isOld;
    }

    public void setIsOld(String isOld) {
        this.isOld = isOld;
    }

    public String getIsBlack() {
        return isBlack;
    }

    public void setIsBlack(String isBlack) {
        this.isBlack = isBlack;
    }

    public String getPushChannelId() {
        return pushChannelId;
    }

    public void setPushChannelId(String pushChannelId) {
        this.pushChannelId = pushChannelId;
    }

    public String getSourceChannelId() {
        return sourceChannelId;
    }

    ;

    public void setSourceChannelId(String sourceChannelId) {
        this.sourceChannelId = sourceChannelId;
    }

    /**
     * 校验基本参数是否为空
     */
    public void checkVildParams() throws CheckException {
        if (this.getMerchantId() == null || "".equals(this.getMerchantId())) {
            throw new CheckException("商户Id不能为空！");
        }
        if (this.getMerchantProductId() == null || "".equals(this.getMerchantProductId())) {
            throw new CheckException("商品Id不能为空！");
        }
        if (this.getUserPhone() == null || "".equals(this.getUserPhone())) {
            throw new CheckException("用户手机号码不能为空！");
        }
        if (this.getIsOld() == null || "".equals(this.getIsOld())) {
            throw new CheckException("是否为老用户标志不能为空！");
        }
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    ;
}
