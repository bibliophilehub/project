package com.inext.entity;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;


/**
 * 订单物流信息
 */
public class AssetLogisticsOrder implements Serializable {

    private static final long serialVersionUID = 8098342754628169918L;

    /**
     * 商品状态 --> 不合格
     */
    public static final Integer AUDIT_STATUS_FAIL = 0;

    /**
     * 商品状态 --> 合格
     */
    public static final Integer AUDIT_STATUS_PASS = 1;


    @Id
    private Integer id;
    private Integer orderId;//借款订单id
    private String ogisticsNo;//快递单号
    private String ogisticsName;//快递公司
    private Date createTime;//邮寄时间
    private Integer auditStatus;//质检状态，0不合格，1合格
    private Date updateTime;//修改时间
    private String updateAccount;//修改人

    @Transient
    private String userPhone;//用户手机号码
    @Transient
    private String userName;//用户姓名
    @Transient
    private Integer moneyAmount;//借款金额，单位为分
    @Transient
    private Integer perPayMoney;//
    @Transient
    private Integer moneyDay;//借款期限/天
    @Transient
    private Integer penaltyAmount;//违约金，单位为分
    @Transient
    private String deviceModel;//手机型号
    @Transient
    private String deviceNumber;//设备号
    @Transient
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getOgisticsNo() {
        return ogisticsNo;
    }

    public void setOgisticsNo(String ogisticsNo) {
        this.ogisticsNo = ogisticsNo;
    }

    public String getOgisticsName() {
        return ogisticsName;
    }

    public void setOgisticsName(String ogisticsName) {
        this.ogisticsName = ogisticsName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
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

    public Integer getMoneyAmount() {
        return moneyAmount;
    }

    public void setMoneyAmount(Integer moneyAmount) {
        this.moneyAmount = moneyAmount;
    }

    public Integer getMoneyDay() {
        return moneyDay;
    }

    public void setMoneyDay(Integer moneyDay) {
        this.moneyDay = moneyDay;
    }

    public Integer getPenaltyAmount() {
        return penaltyAmount;
    }

    public void setPenaltyAmount(Integer penaltyAmount) {
        this.penaltyAmount = penaltyAmount;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

	public Integer getPerPayMoney() {
		return perPayMoney;
	}

	public void setPerPayMoney(Integer perPayMoney) {
		this.perPayMoney = perPayMoney;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}


}
