package com.inext.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 汇潮
 */
public class HcRepaymentInfo {

    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;
    private Integer assetId;//'订单号',
    private BigDecimal reqAmt;//'请求金额',
    private Integer type;//'1取消订单 2续期
    private Integer isSuc;//'0正在处理，1成功，2失败',
    private String orderId;//'汇潮订单号',
    private BigDecimal resAmt;//'汇潮返回金额',
    private String aliNo;//支付宝订单号
    private String resMsg;//汇潮响应信息
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

    public String getAliNo() {
        return aliNo;
    }

    public void setAliNo(String aliNo) {
        this.aliNo = aliNo;
    }

    public String getResMsg() {
        return resMsg;
    }

    public void setResMsg(String resMsg) {
        this.resMsg = resMsg;
    }
}
