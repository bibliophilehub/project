package com.inext.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 *
 * @version
 *
 */
public class AssetBorrowOrder implements Serializable {

    private static final long serialVersionUID = 8098342754628169918L;

    /**订单状态 1待审核，2待放款，3放款成功，4放款失败，5审核拒绝  **/
    /** 待审核
     */
    public static final Integer STATUS_DSH = 1;
    /** 待放款
     */
    public static final Integer STATUS_DFK = 2;
    /** 放款成功
     */
    public static final Integer STATUS_YFK = 3;
    /**
     * 放款失败
     */
    public static final Integer STATUS_FKSB = 4;
    /**
     * 审核拒绝
     */
    public static final Integer STATUS_SHJJ = 5;
    /**
     * 处理中
     */
    public static final Integer STATUS_FKZ = 6;



    /**初始状态**/
    public static final String SUB_PAY_CSZT ="0";
    /**网络异常**/
    public static final String SUB_NERWORK_ERROR ="1";
    /**放款失败 **/
    public static final String SUB_PAY_ERROR ="2";
    /** 提交成功，需要查询结果  **/
    public static final String STATUS_SUB_SUBMIT="3";
    /**支付成功**/
    public static final String SUB_PAY_SUCC ="4";
    /** 疑似重复订单，确认付款接口  **/
    public static final String STATUS_GO_SUBMIT="5";
    /** 订单不存在  **/
    public static final String STATUS_NOT_HAVE="6";



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
    private Integer status; //1 审核中 2 待打款 3 待寄出 5 审核拒绝 7 已违约 8 已取消 10 已寄出 11 已完成 12待收款
    private String confirmCode;//用于确认付款时的验证码, （校验疑似重复或信息不匹配是返回）
    private String noOrder;//商户付款流水号
    private String payStatus;//付款状态： 0：初始状态，1：成功请求到银行，2：请求失败，3：请求成功等待结果；4放款成功
    private String payRemark;
    private String deviceNumber;
    private String deviceModel;
    private String deviceType;
    private String userCardCode;
    private String userCardType;
    private String channelName;
    private Date hisTime;
    private Integer orderEnd;
    private Date updateTime;
    private Integer lateDay;
    private BigDecimal lateMoney;
    private Integer applyNew; // 首次申请 0-是 1-否

    /**
     * 支付通道
     * 具体值参阅 PaymentChannelEnum 枚举介绍
     */
    private Integer paymentChannel;

    /**
     * 是否推送到口袋理财
     */
    private Integer isPush;

    @Transient
    private String score;
    @Transient
    private String detail;

    public String getDetail() {
        return detail;
    }

    public String getScore() {
        return score;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setScore(String score) {
        this.score = score;
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

    public Date getHisTime() {
        return hisTime;
    }

    public void setHisTime(Date hisTime) {
        this.hisTime = hisTime;
    }

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

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

    public Integer getApplyNew() {
        return applyNew;
    }

    public void setApplyNew(Integer applyNew) {
        this.applyNew = applyNew;
    }

    public Integer getPaymentChannel() {
        return paymentChannel;
    }

    public void setPaymentChannel(Integer paymentChannel) {
        this.paymentChannel = paymentChannel;
    }

    public Integer getIsPush() {
        return isPush;
    }

    public void setIsPush(Integer isPush) {
        this.isPush = isPush;
    }
}
