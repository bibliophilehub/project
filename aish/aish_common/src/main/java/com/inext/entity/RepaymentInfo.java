package com.inext.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: jzhang
 * @Date: 2018-04-12 0012 下午 04:36
 */
public class RepaymentInfo {

    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;
    private Integer assetId;//'订单号',
    private BigDecimal reqAmt;//'请求金额',
    private Integer type;//'1取消订单 2续期
    private Integer isSuc;//'0正在处理，1成功，2失败',
    private String orderId;//'富友订单号',
    private BigDecimal resAmt;//'富友返回金额',
    private Date createTime;
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAssetId() {
        return assetId;
    }

    public void setAssetId(Integer assetId) {
        this.assetId = assetId;
    }

    public BigDecimal getReqAmt() {
        return reqAmt;
    }

    public void setReqAmt(BigDecimal reqAmt) {
        this.reqAmt = reqAmt;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getIsSuc() {
        return isSuc;
    }

    public void setIsSuc(Integer isSuc) {
        this.isSuc = isSuc;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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
