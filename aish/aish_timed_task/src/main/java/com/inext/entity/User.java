package com.inext.entity;

import com.inext.entity.base.BaseEntity;
import com.inext.entity.base.TableName;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 开发人员：jzhang
 * 创建时间：2017-04-27 上午 10:16
 */
public class User extends BaseEntity {

    protected Integer id;
    protected String userAccount;
    protected Integer userType;
    protected String userPassword;
    protected String userPhone;
    protected Integer phoneStatus;
    protected String avatarImg;
    protected char userSex;
    protected Integer userProfessional;
    protected String userQq;
    protected String userEmeal;
    protected Integer emailStatus;
    protected String userRealname;
    protected Integer userAge;
    protected String cardNumber;
    protected Integer realnameStauts;
    protected Date realnameTime;
    protected BigDecimal taxpreIncome;
    protected Integer sesameCredit;
    protected Integer publicFund;
    protected Integer workHours;
    protected Integer onlineShoppingFrequency;
    protected Integer creditCard;
    protected Integer creditMonthBill;
    protected Integer userCar;
    protected Integer userHouse;
    protected String userProvince;
    protected String userCity;
    protected String userArea;
    protected String userAddress;
    protected Date addTime;
    protected String registeredIp;
    protected String loginIp;
    protected Date user_loginTime;
    protected Date updateTime;
    protected String remark;
    protected String status;
    protected String source;

    //查询辅助字段
    protected Date ltAddTime;
    protected Date qtAddTime;

    @Override
    public TableName getTableName() {
        return TableName.borrow_user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public Integer getPhoneStatus() {
        return phoneStatus;
    }

    public void setPhoneStatus(Integer phoneStatus) {
        this.phoneStatus = phoneStatus;
    }

    public String getAvatarImg() {
        return avatarImg;
    }

    public void setAvatarImg(String avatarImg) {
        this.avatarImg = avatarImg;
    }

    public char getUserSex() {
        return userSex;
    }

    public void setUserSex(char userSex) {
        this.userSex = userSex;
    }

    public Integer getUserProfessional() {
        return userProfessional;
    }

    public void setUserProfessional(Integer userProfessional) {
        this.userProfessional = userProfessional;
    }

    public String getUserQq() {
        return userQq;
    }

    public void setUserQq(String userQq) {
        this.userQq = userQq;
    }

    public String getUserEmeal() {
        return userEmeal;
    }

    public void setUserEmeal(String userEmeal) {
        this.userEmeal = userEmeal;
    }

    public Integer getEmailStatus() {
        return emailStatus;
    }

    public void setEmailStatus(Integer emailStatus) {
        this.emailStatus = emailStatus;
    }

    public String getUserRealname() {
        return userRealname;
    }

    public void setUserRealname(String userRealname) {
        this.userRealname = userRealname;
    }

    public Integer getUserAge() {
        return userAge;
    }

    public void setUserAge(Integer userAge) {
        this.userAge = userAge;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Integer getRealnameStauts() {
        return realnameStauts;
    }

    public void setRealnameStauts(Integer realnameStauts) {
        this.realnameStauts = realnameStauts;
    }

    public Date getRealnameTime() {
        return realnameTime;
    }

    public void setRealnameTime(Date realnameTime) {
        this.realnameTime = realnameTime;
    }

    public BigDecimal getTaxpreIncome() {
        return taxpreIncome;
    }

    public void setTaxpreIncome(BigDecimal taxpreIncome) {
        this.taxpreIncome = taxpreIncome;
    }

    public Integer getSesameCredit() {
        return sesameCredit;
    }

    public void setSesameCredit(Integer sesameCredit) {
        this.sesameCredit = sesameCredit;
    }

    public Integer getPublicFund() {
        return publicFund;
    }

    public void setPublicFund(Integer publicFund) {
        this.publicFund = publicFund;
    }

    public Integer getWorkHours() {
        return workHours;
    }

    public void setWorkHours(Integer workHours) {
        this.workHours = workHours;
    }

    public Integer getOnlineShoppingFrequency() {
        return onlineShoppingFrequency;
    }

    public void setOnlineShoppingFrequency(Integer onlineShoppingFrequency) {
        this.onlineShoppingFrequency = onlineShoppingFrequency;
    }

    public Integer getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(Integer creditCard) {
        this.creditCard = creditCard;
    }

    public Integer getCreditMonthBill() {
        return creditMonthBill;
    }

    public void setCreditMonthBill(Integer creditMonthBill) {
        this.creditMonthBill = creditMonthBill;
    }

    public Integer getUserCar() {
        return userCar;
    }

    public void setUserCar(Integer userCar) {
        this.userCar = userCar;
    }

    public Integer getUserHouse() {
        return userHouse;
    }

    public void setUserHouse(Integer userHouse) {
        this.userHouse = userHouse;
    }

    public String getUserProvince() {
        return userProvince;
    }

    public void setUserProvince(String userProvince) {
        this.userProvince = userProvince;
    }

    public String getUserCity() {
        return userCity;
    }

    public void setUserCity(String userCity) {
        this.userCity = userCity;
    }

    public String getUserArea() {
        return userArea;
    }

    public void setUserArea(String userArea) {
        this.userArea = userArea;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public String getRegisteredIp() {
        return registeredIp;
    }

    public void setRegisteredIp(String registeredIp) {
        this.registeredIp = registeredIp;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public Date getUser_loginTime() {
        return user_loginTime;
    }

    public void setUser_loginTime(Date user_loginTime) {
        this.user_loginTime = user_loginTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Date getLtAddTime() {
        return ltAddTime;
    }

    public void setLtAddTime(Date ltAddTime) {
        this.ltAddTime = ltAddTime;
    }

    public Date getQtAddTime() {
        return qtAddTime;
    }

    public void setQtAddTime(Date qtAddTime) {
        this.qtAddTime = qtAddTime;
    }
}
