package com.inext.entity;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by 李思鸽 on 2017/3/9 0009.
 */
public class SysModule implements Serializable {
    // 最高级
    public final static Integer SUPER = 0;
    // 是否有效 有效
    public static final int ENABLE_STATUS_USABLE = 1;
    // 是否有效 无效
    public static final int ENABLE_STATUS_DISABLED = 0;
    // 是否显示 隐藏
    public static final int DISPLAT_STATUS_HIDE = 0;
    // 是否显示 显示
    public static final int DISPLAT_STATUS_SHOW = 1;
    private static final long serialVersionUID = 8098342754628169918L;
    @Id
    private Integer id;
    private Integer parentId;

    private String name;
    private String url;
    private String iconClass;
    private String elementClass;

    private Integer menus;
    private Integer status;
    private Integer rank;

    private String remark;

    private Date createTime;
    private Date updateTime;
    private String createAccount;//创建人
    private String updateAccount;//更新人
    private Integer enabled;
    @Transient
    private List<SysModule> children;
    @Transient
    private String moduleNo;//编号

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIconClass() {
        return iconClass;
    }

    public void setIconClass(String iconClass) {
        this.iconClass = iconClass;
    }

    public String getElementClass() {
        return elementClass;
    }

    public void setElementClass(String elementClass) {
        this.elementClass = elementClass;
    }

    public Integer getMenus() {
        return menus;
    }

    public void setMenus(Integer menus) {
        this.menus = menus;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public List<SysModule> getChildren() {
        return children;
    }

    public void setChildren(List<SysModule> children) {
        this.children = children;
    }

    public String getModuleNo() {
        if (id != null) {
            return "M" + String.format("%0" + 3 + "d", id);
        }
        return this.moduleNo;
    }

    public void setModuleNo(String moduleNo) {
        this.moduleNo = moduleNo;
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

}
