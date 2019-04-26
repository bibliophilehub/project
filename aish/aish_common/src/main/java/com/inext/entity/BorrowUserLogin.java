package com.inext.entity;

import java.util.Date;

/**
 * app用戶登录历史表
 *
 * @author ttj
 */
public class BorrowUserLogin {
    private int id;//`id` int(11) NOT NULL AUTO_INCREMENT,
    private Integer userId;//  `userId` int(11) NOT NULL COMMENT '用户ID',
    private String loginIp;//  `loginIp` varchar(25) NOT NULL DEFAULT '' COMMENT '登录IP',
    private Date createTime;//  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
