package com.inext.entity;

import java.util.Date;

public class BorrowUserMessage {
    private int id;//		  `id` int(19) NOT NULL AUTO_INCREMENT,
    private String title;//		  `title` varchar(512) DEFAULT NULL COMMENT '消息标题',
    private String content;//	  `content` varchar(2048) DEFAULT NULL COMMENT '消息内容',
    private String type;//	  `type` int(19) DEFAULT NULL COMMENT '信息类型',
    private Integer userId;//		  `userId` int(19) DEFAULT NULL COMMENT '用户id',
    private Date createTime;//		  `createTime` datetime DEFAULT NULL COMMENT '消息创建时间',
    private Date updateTime;// 修改时间
    private Integer isread;//是否已读
    private Integer status;//

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getIsread() {
        return isread;
    }

    public void setIsread(Integer isread) {
        this.isread = isread;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }


}
