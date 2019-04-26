package com.inext.entity;

import com.inext.utils.DateUtils;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * @author xieyaling
 * @name用户app登录信息类
 * @date 2016-11-16
 */
@Table(name = "app_auto_login_log")
public class AppAutoLoginLog {
    private static final long serialVersionUID = 5254404498182628253L;

    @Id
    private Integer id;//编号

    private Integer userId;//用户编号

    private String userAccount;//用户名称

    private String userPassword;//登录密码

    private Date loginTime;//登录时间

    private Date effTime;//失效时间

    private String token;//用户登录唯一token标识

    private String loginAddress;//登陆地
    private String loginMode;//登陆端（app wx html5）
    private String pushlistChannelName;//发布渠道
    private String appVersion;//app版本
    private String deviceFactory;//设备厂
    private String deviceVersion;//设备型号
    private String deviceName;//设备名称
    private String deviceSystem;//设备操作系统
    private String deviceSystemVersion;//设备系统版本号
    private String deviceMachineNo;//设备机器号
    private String loginLatitude;//登陆纬度
    private String loginLongitude;//登陆经度
    private Integer channelPathId;//导流渠道路径
    private Long pushlistChannelId;//发布渠道
    private String appPlatform;//app平台

    @Transient
    private String idNo;//字符串编号
    @Transient
    private String pushlistChannelName1;
    @Transient
    private Integer pushlistChannelType;
    @Transient
    private String channelName;
    @Transient
    private String urlName;

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

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount == null ? null : userAccount.trim();
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword == null ? null : userPassword.trim();
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public Date getEffTime() {
        return effTime;
    }

    public void setEffTime(Date effTime) {
        this.effTime = effTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token == null ? null : token.trim();
    }

    public String getLoginAddress() {
        return loginAddress;
    }

    public void setLoginAddress(String loginAddress) {
        this.loginAddress = loginAddress;
    }

    public String getLoginMode() {
        return loginMode;
    }

    public void setLoginMode(String loginMode) {
        this.loginMode = loginMode;
    }

    public String getPushlistChannelName() {
        return pushlistChannelName;
    }

    public void setPushlistChannelName(String pushlistChannelName) {
        this.pushlistChannelName = pushlistChannelName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getDeviceFactory() {
        return deviceFactory;
    }

    public void setDeviceFactory(String deviceFactory) {
        this.deviceFactory = deviceFactory;
    }

    public String getDeviceVersion() {
        return deviceVersion;
    }

    public void setDeviceVersion(String deviceVersion) {
        this.deviceVersion = deviceVersion;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceSystem() {
        return deviceSystem;
    }

    public void setDeviceSystem(String deviceSystem) {
        this.deviceSystem = deviceSystem;
    }

    public String getDeviceSystemVersion() {
        return deviceSystemVersion;
    }

    public void setDeviceSystemVersion(String deviceSystemVersion) {
        this.deviceSystemVersion = deviceSystemVersion;
    }

    public String getDeviceMachineNo() {
        return deviceMachineNo;
    }

    public void setDeviceMachineNo(String deviceMachineNo) {
        this.deviceMachineNo = deviceMachineNo;
    }

    public String getLoginLatitude() {
        return loginLatitude;
    }

    public void setLoginLatitude(String loginLatitude) {
        this.loginLatitude = loginLatitude;
    }

    public String getLoginLongitude() {
        return loginLongitude;
    }

    public void setLoginLongitude(String loginLongitude) {
        this.loginLongitude = loginLongitude;
    }

    public String getIdNo() {
        if (id != null) {
            return "SU" + DateUtils.formatDate(this.getLoginTime(), DateUtils.FORMAT) + String.format("%0" + 10 + "d", id);
        }
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public Integer getChannelPathId() {
        return channelPathId;
    }

    public void setChannelPathId(Integer channelPathId) {
        this.channelPathId = channelPathId;
    }

    public Long getPushlistChannelId() {
        return pushlistChannelId;
    }

    public void setPushlistChannelId(Long pushlistChannelId) {
        this.pushlistChannelId = pushlistChannelId;
    }

    public String getPushlistChannelName1() {
        return pushlistChannelName1;
    }

    public void setPushlistChannelName1(String pushlistChannelName1) {
        this.pushlistChannelName1 = pushlistChannelName1;
    }

    public Integer getPushlistChannelType() {
        return pushlistChannelType;
    }

    public void setPushlistChannelType(Integer pushlistChannelType) {
        this.pushlistChannelType = pushlistChannelType;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getUrlName() {
        return urlName;
    }

    public void setUrlName(String urlName) {
        this.urlName = urlName;
    }

    public String getAppPlatform() {
        return appPlatform;
    }

    public void setAppPlatform(String appPlatform) {
        this.appPlatform = appPlatform;
    }

}