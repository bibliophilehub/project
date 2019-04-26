package com.inext.entity;

import java.util.Date;

public class BorrowUserJxl {
    private int id;//`id` int(19) NOT NULL,
    private Integer userId;//  `userId` int(19) DEFAULT NULL COMMENT '用户id',
    private String token;//  `token` varchar(1024) DEFAULT NULL COMMENT '用户服务器认证token',
    private String content;//  `content` longtext COMMENT '聚信立报告',
    private Date createTime;//  `createTime` datetime DEFAULT NULL,
    private Date updateTime;//  `updateTime` datetime DEFAULT NULL,

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
}
