package com.inext.entity;

import java.util.Date;

public class OutOrders {
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

    public static final String TYPE_FUYOU = "FUYOU";
    public static final String TYPE_LIANLIAN = "LIANLIAN";
    public static final String TYPE_BAOFOO = "BAOFOO";
    public static final String TYPE_HELI = "HELI";
    public static final String TYPE_HELID1 = "HELID1";
    public static final String TYPE_KOUDAI = "KOUDAI";
    public static final String TYPE_HUICHAO = "HUICHAO";//汇潮

    public static final String act_AgentRequest_A = "AgentRequest_A";

    /**
     * 查询订单
     */
    public static final String QUERY_ORDER = "QUERY_ORDER";

    /**
     * 推送资产订单
     */
    public static final String PUSH_ACTIVO_ORDER = "PUSH_ACTIVO_ORDER";


    private Integer id;
    private String userId;
    private Integer assetOrderId;
    private String orderType;
    private String orderNo;
    private String act;
    private String tablelastName;//表面后缀
    private String reqParams;
    private String returnParams;
    private Date notifyTime;
    private String notifyParams;
    private Date addTime;
    private String addIp;
    private Date updateTime;
    private String status;

    public String getTablelastName() {
        return tablelastName;
    }

    public void setTablelastName(String tablelastName) {
        this.tablelastName = tablelastName;
    }

    public String getNotifyParams() {
        return notifyParams;
    }

    public void setNotifyParams(String notifyParams) {
        this.notifyParams = notifyParams;
    }

    public Integer getAssetOrderId() {
        return assetOrderId;
    }

    public void setAssetOrderId(Integer assetOrderId) {
        this.assetOrderId = assetOrderId;
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
        return "RiskOrders [userId=" + userId + ", orderType=" + orderType + ", act=" + act + ", addIp=" + addIp + ", addTime=" + addTime + ", id=" + id + ", notifyParams=" + notifyParams + ", notifyTime=" + notifyTime + ", orderNo=" + orderNo + ", reqParams=" + reqParams + ", returnParams=" + returnParams + ", status=" + status + ", updateTime=" + updateTime + ", tablelastName=" + tablelastName + "]";
    }

}
