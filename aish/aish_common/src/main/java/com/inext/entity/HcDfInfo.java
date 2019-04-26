package com.inext.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

public class HcDfInfo {
    /**
     * 该笔请求等待中
     */
    public static final String STATUS_WAIT = "0";
    /**
     * 该笔请求的非成功状态
     */
    public static final String STATUS_OTHER = "1";
    /**
     * 该笔请求的成功状态
     */
    public static final String STATUS_SUC = "2";

    @Id
//    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @GeneratedValue(generator = "JDBC")
    private Integer id;
    private String operateUserId;
    private Integer assetOrderId;
    private String type;
    private String bankCardNo;//
    private String userName;
    private String orderNo;
    private String reqAmt;
    private String reqParams;
    private String returnParams;
    private Date notifyTime;
    private String notifyParams;
    private Date addTime;
    private String addIp;
    private Date updateTime;
    private String status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOperateUserId() {
        return operateUserId;
    }

    public void setOperateUserId(String operateUserId) {
        this.operateUserId = operateUserId;
    }

    public Integer getAssetOrderId() {
        return assetOrderId;
    }

    public void setAssetOrderId(Integer assetOrderId) {
        this.assetOrderId = assetOrderId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getReqAmt() {
        return reqAmt;
    }

    public void setReqAmt(String reqAmt) {
        this.reqAmt = reqAmt;
    }

    public String getReqParams() {
        return reqParams;
    }

    public void setReqParams(String reqParams) {
        this.reqParams = reqParams;
    }

    public String getReturnParams() {
        return returnParams;
    }

    public void setReturnParams(String returnParams) {
        this.returnParams = returnParams;
    }

    public Date getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(Date notifyTime) {
        this.notifyTime = notifyTime;
    }

    public String getNotifyParams() {
        return notifyParams;
    }

    public void setNotifyParams(String notifyParams) {
        this.notifyParams = notifyParams;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public String getAddIp() {
        return addIp;
    }

    public void setAddIp(String addIp) {
        this.addIp = addIp;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

//    @Override
//    public String toString() {
//        return "RiskOrders [userId=" + userId + ", orderType=" + orderType + ", act=" + act + ", addIp=" + addIp + ", addTime=" + addTime + ", id=" + id + ", notifyParams=" + notifyParams + ", notifyTime=" + notifyTime + ", orderNo=" + orderNo + ", reqParams=" + reqParams + ", returnParams=" + returnParams + ", status=" + status + ", updateTime=" + updateTime + ", tablelastName=" + tablelastName + "]";
//    }

}
