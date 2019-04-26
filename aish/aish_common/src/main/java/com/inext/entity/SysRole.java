package com.inext.entity;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

/**
 * Created by 李思鸽 on 2017/3/9 0009.
 */
public class SysRole {

    // 最高级角色上级
    public final static Integer SUPER = 0;
    // 超级管理员角色
    public final static Integer SUPERROLE = 1;
    // 是否有效 有效
    public static final int ENABLE_STATUS_USABLE = 1;

    // 是否有效 无效
    public static final int ENABLE_STATUS_DISABLED = 0;

    // 是否显示 隐藏
    public static final int DISPLAT_STATUS_HIDE = 0;

    // 是否显示 显示
    public static final int DISPLAT_STATUS_SHOW = 1;
    @Id
    private Integer id;
    private Integer parentId;
    private Integer rank;
    private String name;

    private Integer status;

    private String remark;

    private Date createTime;
    private Date updateTime;
    private String createAccount;//创建人
    private String updateAccount;//更新人
    private Integer enabled;
    @Transient
    private String roleNo;
    @Transient
    private List<SysRole> children;
    @Transient
    private String roleModules;//角色菜单

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public List<SysRole> getChildren() {
        return children;
    }

    public void setChildren(List<SysRole> children) {
        this.children = children;
    }

    public String getRoleNo() {
        if (id != null) {
            return "M" + String.format("%0" + 3 + "d", id);
        } else {
            return roleNo;
        }
    }

    public void setRoleNo(String roleNo) {
        this.roleNo = roleNo;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public String getRoleModules() {
        return roleModules;
    }

    public void setRoleModules(String roleModules) {
        this.roleModules = roleModules;
    }

}
