package com.inext.entity;

import javax.persistence.Id;
import java.util.Date;

public class RiskOrders {
    /**
     * 该笔请求的非成功状态
     */
    public static final String STATUS_OTHER = "1";
    /**
     * 该笔请求的成功状态
     */
    public static final String STATUS_SUC = "2";

    @Id
    private Integer id;
    private String userId;
    private String orderType;
    private String orderNo;
    private String act;
    private String reqParams;
    private String returnParams;
    private Date notifyTime;
    private String notifyParams;
    private Date addTime;
    private String addIp;
    private Date updateTime;
    private String status;

    public String getNotifyParams() {
        return notifyParams;
    }

    public void setNotifyParams(String notifyParams) {
        this.notifyParams = notifyParams;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getAct() {
        return act;
    }

    public void setAct(String act) {
        this.act = act;
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

    @Override
    public String toString() {
        return "RiskOrders [userId=" + userId + ", orderType=" + orderType
                + ", act=" + act + ", addIp=" + addIp + ", addTime=" + addTime
                + ", id=" + id + ", notifyParams=" + notifyParams
                + ", notifyTime=" + notifyTime + ", orderNo=" + orderNo
                + ", reqParams=" + reqParams + ", returnParams=" + returnParams
                + ", status=" + status + ", updateTime=" + updateTime + "]";
    }

}
