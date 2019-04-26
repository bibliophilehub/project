package com.inext.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

public class AssetRepaymentOrderDaikou {
    
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    private Integer repaymentOrderId;//还款订单id

    private BigDecimal reqAmt;//代扣请求扣款金额

    private String payOrderId;//代扣商务订单号

    private Integer payBussiness;//1易宝 2先锋

    private Integer status;//我方代扣处理状态： 0初始状态，1 处理中，2成功，3失败

    private String chargeStatus;//代扣方响应状态

    private String dkOrderId;//代扣方流水号

    private BigDecimal resAmt;//代扣方返回实际扣款值

    private Date createTime;

    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRepaymentOrderId() {
        return repaymentOrderId;
    }

    public void setRepaymentOrderId(Integer repaymentOrderId) {
        this.repaymentOrderId = repaymentOrderId;
    }

    public BigDecimal getReqAmt() {
        return reqAmt;
    }

    public void setReqAmt(BigDecimal reqAmt) {
        this.reqAmt = reqAmt;
    }

    public String getPayOrderId() {
        return payOrderId;
    }

    public void setPayOrderId(String payOrderId) {
        this.payOrderId = payOrderId == null ? null : payOrderId.trim();
    }

    public Integer getPayBussiness() {
        return payBussiness;
    }

    public void setPayBussiness(Integer payBussiness) {
        this.payBussiness = payBussiness;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getChargeStatus() {
        return chargeStatus;
    }

    public void setChargeStatus(String chargeStatus) {
        this.chargeStatus = chargeStatus == null ? null : chargeStatus.trim();
    }

    public String getDkOrderId() {
        return dkOrderId;
    }

    public void setDkOrderId(String dkOrderId) {
        this.dkOrderId = dkOrderId == null ? null : dkOrderId.trim();
    }

    public BigDecimal getResAmt() {
        return resAmt;
    }

    public void setResAmt(BigDecimal resAmt) {
        this.resAmt = resAmt;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}