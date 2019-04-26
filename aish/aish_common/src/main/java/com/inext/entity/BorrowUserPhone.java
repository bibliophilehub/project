package com.inext.entity;

import java.util.Date;

/**
 * app用户通讯录
 *
 * @author admin
 */
public class BorrowUserPhone {
    private int id;//`id` int(19) NOT NULL AUTO_INCREMENT,
    private String name;//  `name` varchar(255) DEFAULT NULL COMMENT '通讯录名称',
    private String phone;//  `phone` varchar(255) DEFAULT NULL COMMENT '通讯录手机号码',
    private Integer userId;//  `userId` int(19) DEFAULT NULL COMMENT 'app用户id',
    private Date createTime;//  `createTime` datetime DEFAULT NULL COMMENT '创建时间',

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

}
