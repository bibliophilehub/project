package com.inext.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

public class AssetRepaymentOrderWithhold {
    
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    private BigDecimal reqAmt;//代扣请求扣款金额

    private String requestId;//代扣商务订单号

    private Integer payBussiness;//1易宝 2先锋

    private Integer status;//我方代扣处理状态： 0初始状态，1 处理中，2成功，3失败

    private Integer chargeStatus;//代扣方响应状态

    private String batchId;//代扣方流水号

    private BigDecimal resAmt;//代扣方返回实际扣款值

    private Date createTime;//创建时间

    private Date updateTime;//修改时间

    private String errMessage;//失败原因

    private Integer reqNum;//代扣订单数

    private Integer resNum;//代扣实际成功订单数

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getReqAmt() {
        return reqAmt;
    }

    public void setReqAmt(BigDecimal reqAmt) {
        this.reqAmt = reqAmt;
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

    public Integer getChargeStatus() {
        return chargeStatus;
    }

    public void setChargeStatus(Integer chargeStatus) {
        this.chargeStatus = chargeStatus;
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

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }

    public Integer getReqNum() {
        return reqNum;
    }

    public void setReqNum(Integer reqNum) {
        this.reqNum = reqNum;
    }

    public Integer getResNum() {
        return resNum;
    }

    public void setResNum(Integer resNum) {
        this.resNum = resNum;
    }
}