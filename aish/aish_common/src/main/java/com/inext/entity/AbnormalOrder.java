package com.inext.entity;

import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * 重复扣款、代扣后续期订单
 */
@Data
public class AbnormalOrder implements Serializable {
    public static Map<Integer, Object> refundIssueMap = new LinkedHashMap<Integer, Object>();

    /**
     * 重复还款
     */
    public static final Integer REPEATED_REPAYMENT= 1;
    /**
     * 还款后续期
     */
    public static final Integer PERIOD_AFTER_REPAYMENT = 2;
    /**
     * 其他
     */
    public static final Integer OTHER = 3;

    static {
        refundIssueMap.put(REPEATED_REPAYMENT, "重复还款");
        refundIssueMap.put(PERIOD_AFTER_REPAYMENT, "还款后续期");
        refundIssueMap.put(OTHER, "其他");
    }



    private Integer id;
    private Integer userId;
    private String userName;
    private Integer orderId;
    private String bank;
    private String cardCode;
    private String repaymentOrderNo;
    private String repaymentMoney;
    private Integer issue;
    private String refundOrderNo;
    private Integer refundChannel;
    private String refundBank;
    private String refundCardNo;
    private String amount;
    private String operator;
    private String remark;
    private Integer repaymentChannel;
    private Integer treatmentMarks;
    private Date creatTime;
    @Transient
    private String nickName;
    @Transient
    private String userPhone;

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

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public String getRepaymentOrderNo() {
        return repaymentOrderNo;
    }

    public void setRepaymentOrderNo(String repaymentOrderNo) {
        this.repaymentOrderNo = repaymentOrderNo;
    }

    public String getRepaymentMoney() {
        return repaymentMoney;
    }

    public void setRepaymentMoney(String repaymentMoney) {
        this.repaymentMoney = repaymentMoney;
    }

    public Integer getIssue() {
        return issue;
    }

    public void setIssue(Integer issue) {
        this.issue = issue;
    }

    public String getRefundOrderNo() {
        return refundOrderNo;
    }

    public void setRefundOrderNo(String refundOrderNo) {
        this.refundOrderNo = refundOrderNo;
    }

    public Integer getRefundChannel() {
        return refundChannel;
    }

    public void setRefundChannel(Integer refundChannel) {
        this.refundChannel = refundChannel;
    }

    public String getRefundBank() {
        return refundBank;
    }

    public void setRefundBank(String refundBank) {
        this.refundBank = refundBank;
    }

    public String getRefundCardNo() {
        return refundCardNo;
    }

    public void setRefundCardNo(String refundCardNo) {
        this.refundCardNo = refundCardNo;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getRepaymentChannel() {
        return repaymentChannel;
    }

    public void setRepaymentChannel(Integer repaymentChannel) {
        this.repaymentChannel = repaymentChannel;
    }

    public Integer getTreatmentMarks() {
        return treatmentMarks;
    }

    public void setTreatmentMarks(Integer treatmentMarks) {
        this.treatmentMarks = treatmentMarks;
    }

    public Date getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(Date creatTime) {
        this.creatTime = creatTime;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }
}
