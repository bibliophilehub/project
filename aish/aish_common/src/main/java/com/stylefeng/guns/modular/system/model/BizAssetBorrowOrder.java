package com.stylefeng.guns.modular.system.model;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 *
 * @version
 *
 */
public class BizAssetBorrowOrder implements Serializable {

    private static final long serialVersionUID = 8098342754628169918L;

    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;
    private Integer userId;
    private String userPhone;
    private String userName;
    private String userIdNumber;
    private BigDecimal moneyAmount;
    private Integer moneyDay;
    private BigDecimal penaltyAmount;
    private BigDecimal perPayMoney;
    private Date orderTime;
    private Date loanTime;
    private Date loanEndTime;
    private Date auditTime;
    private BigDecimal lateFeeApr;
    private Integer status; 
    private String confirmCode;//用于确认付款时的验证码, （校验疑似重复或信息不匹配是返回）
    private String noOrder;//商户付款流水号
    private String payStatus;
    private String payRemark;
    private String deviceNumber;
    private String deviceModel;
    private String deviceType;
    private String userCardCode;
    private String userCardType;
//    private String channelName;
//    private Date hisTime;
    private Integer orderEnd;
    private Date updateTime;
    private Integer lateDay;
    private BigDecimal lateMoney;
    private Integer isRisk;
    private String dfType;

    private String refundRemark;

    /**
     * 是否老用户 默认否 是=1 否=0
     */
    private Integer isOld;
    
    public Integer getIsRisk()
    {
        return isRisk;
    }

    public void setIsRisk(Integer isRisk)
    {
        this.isRisk = isRisk;
    }

    public String getPayRemark() {
        return payRemark;
    }

    public void setPayRemark(String payRemark) {
        this.payRemark = payRemark;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserIdNumber() {
        return userIdNumber;
    }

    public void setUserIdNumber(String userIdNumber) {
        this.userIdNumber = userIdNumber;
    }


    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public Date getLoanTime() {
        return loanTime;
    }

    public void setLoanTime(Date loanTime) {
        this.loanTime = loanTime;
    }

    public Date getLoanEndTime() {
        return loanEndTime;
    }

    public void setLoanEndTime(Date loanEndTime) {
        this.loanEndTime = loanEndTime;
    }

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getConfirmCode() {
        return confirmCode;
    }

    public void setConfirmCode(String confirmCode) {
        this.confirmCode = confirmCode;
    }

    public String getNoOrder() {
        return noOrder;
    }

    public void setNoOrder(String noOrder) {
        this.noOrder = noOrder;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getUserCardCode() {
        return userCardCode;
    }

    public void setUserCardCode(String userCardCode) {
        this.userCardCode = userCardCode;
    }

    public String getUserCardType() {
        return userCardType;
    }

    public void setUserCardType(String userCardType) {
        this.userCardType = userCardType;
    }

//    public Date getHisTime() {
//        return hisTime;
//    }
//
//    public void setHisTime(Date hisTime) {
//        this.hisTime = hisTime;
//    }

    public Integer getOrderEnd() {
        return orderEnd;
    }

    public void setOrderEnd(Integer orderEnd) {
        this.orderEnd = orderEnd;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getLateDay() {
        return lateDay;
    }

    public void setLateDay(Integer lateDay) {
        this.lateDay = lateDay;
    }

    public BigDecimal getMoneyAmount() {
        return moneyAmount;
    }

    public void setMoneyAmount(BigDecimal moneyAmount) {
        this.moneyAmount = moneyAmount;
    }

    public Integer getMoneyDay() {
        return moneyDay;
    }

    public void setMoneyDay(Integer moneyDay) {
        this.moneyDay = moneyDay;
    }

    public BigDecimal getPenaltyAmount() {
        return penaltyAmount;
    }

    public void setPenaltyAmount(BigDecimal penaltyAmount) {
        this.penaltyAmount = penaltyAmount;
    }

    public BigDecimal getPerPayMoney() {
        return perPayMoney;
    }

    public void setPerPayMoney(BigDecimal perPayMoney) {
        this.perPayMoney = perPayMoney;
    }

    public BigDecimal getLateFeeApr() {
        return lateFeeApr;
    }

    public void setLateFeeApr(BigDecimal lateFeeApr) {
        this.lateFeeApr = lateFeeApr;
    }

    public BigDecimal getLateMoney() {
        return lateMoney;
    }

    public void setLateMoney(BigDecimal lateMoney) {
        this.lateMoney = lateMoney;
    }

    public Integer getIsOld() {
        return isOld;
    }

    public void setIsOld(Integer isOld) {
        this.isOld = isOld;
    }

    public String getDfType() {
        return dfType;
    }

    public void setDfType(String dfType) {
        this.dfType = dfType;
    }

    public String getRefundRemark() {
        return refundRemark;
    }

    public void setRefundRemark(String refundRemark) {
        this.refundRemark = refundRemark;
    }

    //	public String getChannelName() {
//		return channelName;
//	}
//
//	public void setChannelName(String channelName) {
//		this.channelName = channelName;
//	}
    
}
