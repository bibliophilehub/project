
package com.inext.dto;

import com.inext.entity.AssetRepaymentOrderWithhold;

import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class AssetRepaymentOrderByWithholdDto implements Serializable
{
    private static final long serialVersionUID = 8098342754628169918L;

    @Id
    private Integer id;
    private Integer userId; //用户id
    private String userPhone;//用户手机号码
    private String userName;//用户姓名
    private Integer orderId;//借款订单id
    private BigDecimal moneyAmount; //借款金额，单位为分
    private BigDecimal penaltyAmount;//违约金，单位为分
    private BigDecimal planLateFee;//滞纳金，单位为分
    private BigDecimal repaymentAmount;//总还款金额（本金+服务费+滞纳金），单位为分
    private BigDecimal repaymentedAmount;//已还款金额，单位为分
    private Integer repaymentType;//还款方式（1、邮寄手机2、后台手工3、主动支付）
    private BigDecimal lateFeeApr;//滞纳金利率，单位为万分之几
    private BigDecimal ceditAmount;//总减免金额
    private Date creditRepaymentTime;// 放款时间
    private Date repaymentTime;//应还款时间
    private Integer period;//实际还款总期限
    private Date repaymentRealTime;//实际还款日期
    private Date lateFeeStartTime;//滞纳金计算开始时间
    private Date interestUpdateTime;//滞纳金更新时间
    private Integer lateDay;//逾期天数
    private Integer orderStatus;//还款订单状态：6待还款，7已逾期，8已还款
    private Integer dkPayStatus;//代扣支付状态:0:初始状态；1:处理中；2：成功；3：失败;
    private Integer orderRenewal;//订单是否续期9已续期
    private Integer orderMail;//是否寄出，10已寄出
    private String noOrder;//三方付款订单id
    private Date updateTime;//修改时间
    private String updateAccount;//修改人

    private String errMessage;//失败原因
    private String seqId;//流水号

    private String bankCode;
    private String bankName;
    private String cardCode;
    private String userCardNo;
    private Integer cardType;

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }

    public String getSeqId() {
        return seqId;
    }

    public void setSeqId(String seqId) {
        this.seqId = seqId;
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

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getMoneyAmount() {
        return moneyAmount;
    }

    public void setMoneyAmount(BigDecimal moneyAmount) {
        this.moneyAmount = moneyAmount;
    }

    public BigDecimal getPenaltyAmount() {
        return penaltyAmount;
    }

    public void setPenaltyAmount(BigDecimal penaltyAmount) {
        this.penaltyAmount = penaltyAmount;
    }

    public BigDecimal getPlanLateFee() {
        return planLateFee;
    }

    public void setPlanLateFee(BigDecimal planLateFee) {
        this.planLateFee = planLateFee;
    }

    public BigDecimal getRepaymentAmount() {
        return repaymentAmount;
    }

    public void setRepaymentAmount(BigDecimal repaymentAmount) {
        this.repaymentAmount = repaymentAmount;
    }

    public BigDecimal getRepaymentedAmount() {
        return repaymentedAmount;
    }

    public void setRepaymentedAmount(BigDecimal repaymentedAmount) {
        this.repaymentedAmount = repaymentedAmount;
    }

    public Integer getRepaymentType() {
        return repaymentType;
    }

    public void setRepaymentType(Integer repaymentType) {
        this.repaymentType = repaymentType;
    }

    public BigDecimal getLateFeeApr() {
        return lateFeeApr;
    }

    public void setLateFeeApr(BigDecimal lateFeeApr) {
        this.lateFeeApr = lateFeeApr;
    }

    public BigDecimal getCeditAmount() {
        return ceditAmount;
    }

    public void setCeditAmount(BigDecimal ceditAmount) {
        this.ceditAmount = ceditAmount;
    }

    public Date getCreditRepaymentTime() {
        return creditRepaymentTime;
    }

    public void setCreditRepaymentTime(Date creditRepaymentTime) {
        this.creditRepaymentTime = creditRepaymentTime;
    }

    public Date getRepaymentTime() {
        return repaymentTime;
    }

    public void setRepaymentTime(Date repaymentTime) {
        this.repaymentTime = repaymentTime;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public Date getRepaymentRealTime() {
        return repaymentRealTime;
    }

    public void setRepaymentRealTime(Date repaymentRealTime) {
        this.repaymentRealTime = repaymentRealTime;
    }

    public Date getLateFeeStartTime() {
        return lateFeeStartTime;
    }

    public void setLateFeeStartTime(Date lateFeeStartTime) {
        this.lateFeeStartTime = lateFeeStartTime;
    }

    public Date getInterestUpdateTime() {
        return interestUpdateTime;
    }

    public void setInterestUpdateTime(Date interestUpdateTime) {
        this.interestUpdateTime = interestUpdateTime;
    }

    public Integer getLateDay() {
        return lateDay;
    }

    public void setLateDay(Integer lateDay) {
        this.lateDay = lateDay;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getDkPayStatus() {
        return dkPayStatus;
    }

    public void setDkPayStatus(Integer dkPayStatus) {
        this.dkPayStatus = dkPayStatus;
    }

    public Integer getOrderRenewal() {
        return orderRenewal;
    }

    public void setOrderRenewal(Integer orderRenewal) {
        this.orderRenewal = orderRenewal;
    }

    public Integer getOrderMail() {
        return orderMail;
    }

    public void setOrderMail(Integer orderMail) {
        this.orderMail = orderMail;
    }

    public String getNoOrder() {
        return noOrder;
    }

    public void setNoOrder(String noOrder) {
        this.noOrder = noOrder;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateAccount() {
        return updateAccount;
    }

    public void setUpdateAccount(String updateAccount) {
        this.updateAccount = updateAccount;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public String getUserCardNo() {
        return userCardNo;
    }

    public void setUserCardNo(String userCardNo) {
        this.userCardNo = userCardNo;
    }

    public Integer getCardType() {
        return cardType;
    }

    public void setCardType(Integer cardType) {
        this.cardType = cardType;
    }
}
