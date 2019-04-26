package com.inext.entity;

import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 第三方还款计划表
 */
public class AssetRepaymentDetail implements Serializable {

    private static final long serialVersionUID = 8098342754628169918L;
    @Id
    private Integer id;
    private Integer userId; //用户id
    private Integer orderId;//借款订单id
    private Integer renewalId;//续期订单id
    private Integer repaymentChannel;//还款渠道（1、后台还款，2、线下还款）
    private Integer repaymentType;//还款方式（1、支付宝、2银行卡对公、3、银行卡自动扣款、4、对公银行卡转账）
    private String bankName;//还款银行（还款方式为银行时使用）
    private BigDecimal trueRepaymentMoney;//还款金额
    private String remark;//备注
    private Date createdAt;//创建时间
    private String adminUsername;//还款管理员名称
    private BigDecimal ceditAmount;//还款金额
    private String repaymentOrderNo;//还款订单号,
    private Integer type;//'1取消订单 2续期',

    public Integer getRenewalId() {
        return renewalId;
    }

    public void setRenewalId(Integer renewalId) {
        this.renewalId = renewalId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getRepaymentChannel() {
        return repaymentChannel;
    }

    public void setRepaymentChannel(Integer repaymentChannel) {
        this.repaymentChannel = repaymentChannel;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getRepaymentOrderNo() {
        return repaymentOrderNo;
    }

    public void setRepaymentOrderNo(String repaymentOrderNo) {
        this.repaymentOrderNo = repaymentOrderNo;
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

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getRepaymentType() {
        return repaymentType;
    }

    public void setRepaymentType(Integer repaymentType) {
        this.repaymentType = repaymentType;
    }

    public BigDecimal getTrueRepaymentMoney() {
        return trueRepaymentMoney;
    }

    public void setTrueRepaymentMoney(BigDecimal trueRepaymentMoney) {
        this.trueRepaymentMoney = trueRepaymentMoney;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getAdminUsername() {
        return adminUsername;
    }

    public void setAdminUsername(String adminUsername) {
        this.adminUsername = adminUsername;
    }

    public BigDecimal getCeditAmount() {
        return ceditAmount;
    }

    public void setCeditAmount(BigDecimal ceditAmount) {
        this.ceditAmount = ceditAmount;
    }
}
