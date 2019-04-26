package com.inext.entity;

import java.util.Date;

/**
 * 后台用户操作记录
 */
public class BackUserOptionRecord {
    private String id;
    private String module;
    private String childModule;
    private String log;
    private Date createTime;
    private Integer userId;
    private String exStr;
    private Integer exInt;
    private String userName;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getChildModule() {
        return childModule;
    }

    public void setChildModule(String childModule) {
        this.childModule = childModule;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getExStr() {
        return exStr;
    }

    public void setExStr(String exStr) {
        this.exStr = exStr;
    }

    public Integer getExInt() {
        return exInt;
    }

    public void setExInt(Integer exInt) {
        this.exInt = exInt;
    }


}
