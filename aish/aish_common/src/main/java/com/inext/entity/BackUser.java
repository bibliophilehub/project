package com.inext.entity;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;


/**
 *
 * @version
 *
 */
public class BackUser implements Serializable {

    public static final String BackUserBlacklist = "BackUserBlacklist";//黑名单用户key
    // 超级管理员
    public final static Integer SUPER = 6;
    private static final long serialVersionUID = 8098342754628169918L;
    @Id
    private Integer id;
    private String account;
    private String password;
    private String status;
    private Date createTime;
    private Date updateTime;

    private String createAccount;//创建人
    private String updateAccount;//更新人
    private String userType; //用户类型(01普通用户,02导流渠道,03发布渠道)
    private String diversionChannel; //导流渠道
    private String publishingChannel;//发布渠道
    private String nickName;//昵称
    private String remarks;//备注
    private String company;//公司
    private String department;//部门
    private String post;//岗位
    private String fullName;//姓名
    private String telephone;//电话
    private String email;//邮箱
    private String portraitImg;//头像地址
    private Date userLoginTime;//最后登陆时间

    @Transient
    private Map mappinfo;
    @Transient
    private String userRoles;//用户角色id
    @Transient
    private String userRoleNames;//用户角色名称
    @Transient
    private String userModules;//用户菜单
    @Transient
    private String userNo;//编号
    @Transient
    private String oldPassword;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Map getMappinfo() {
        return mappinfo;
    }

    public void setMappinfo(Map mappinfo) {
        this.mappinfo = mappinfo;
    }

    public String getCreateAccount() {
        return createAccount;
    }

    public void setCreateAccount(String createAccount) {
        this.createAccount = createAccount;
    }

    public String getUpdateAccount() {
        return updateAccount;
    }

    public void setUpdateAccount(String updateAccount) {
        this.updateAccount = updateAccount;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getDiversionChannel() {
        return diversionChannel;
    }

    public void setDiversionChannel(String diversionChannel) {
        this.diversionChannel = diversionChannel;
    }

    public String getPublishingChannel() {
        return publishingChannel;
    }

    public void setPublishingChannel(String publishingChannel) {
        this.publishingChannel = publishingChannel;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPortraitImg() {
        return portraitImg;
    }

    public void setPortraitImg(String portraitImg) {
        this.portraitImg = portraitImg;
    }

    public String getUserNo() {
        if (id != null) {
            return "SU" + userType + String.format("%0" + 6 + "d", id);
        }
        return this.userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public Date getUserLoginTime() {
        return userLoginTime;
    }

    public void setUserLoginTime(Date userLoginTime) {
        this.userLoginTime = userLoginTime;
    }

    public String getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(String userRoles) {
        this.userRoles = userRoles;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getUserModules() {
        return userModules;
    }

    public void setUserModules(String userModules) {
        this.userModules = userModules;
    }

    public String getUserRoleNames() {
        return userRoleNames;
    }

    public void setUserRoleNames(String userRoleNames) {
        this.userRoleNames = userRoleNames;
    }

}
