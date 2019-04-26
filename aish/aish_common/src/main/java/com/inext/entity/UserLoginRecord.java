package com.inext.entity;

import javax.persistence.Id;
import java.util.Date;

/**
 * Created by 李思鸽 on 2017/5/17 0017.
 */
public class UserLoginRecord {

    @Id
    private Integer id;
    private Integer userId;

    private String loginIp;

    private Date createTime;

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

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
