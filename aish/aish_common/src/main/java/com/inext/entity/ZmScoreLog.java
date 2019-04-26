package com.inext.entity;

import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * app芝麻分日志对象
 *
 * @author ttj
 */
@Data
public class ZmScoreLog implements Serializable {

    @Id
    private Integer id;//`id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '序列号',
    private Integer userId;//  用户id
    private String zmScore;// 芝麻分
    private String zmContent;// 回调信息
    private Date createTime;//创建时间


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

    public String getZmScore() {
        return zmScore;
    }

    public void setZmScore(String zmScore) {
        this.zmScore = zmScore;
    }

    public String getZmContent() {
        return zmContent;
    }

    public void setZmContent(String zmContent) {
        this.zmContent = zmContent;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }



}
