package com.inext.entity;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 *
 * @version
 *
 */
@Data
public class ChannelInfo implements Serializable {
    @Id
    //@GeneratedValue(strategy= GenerationType.IDENTITY)
    @GeneratedValue(generator = "JDBC")
    private Integer id;
    private String channelCode;
    private String channelName;
    private String channelUrl;
    private BigDecimal oldUserScore;
    private BigDecimal newUserScore;
    private Date createTime;
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }


    public String getChannelUrl() {
        return channelUrl;
    }

    public void setChannelUrl(String channelUrl) {
        this.channelUrl = channelUrl;
    }

    public BigDecimal getOldUserScore() {
        return oldUserScore;
    }

    public void setOldUserScore(BigDecimal oldUserScore) {
        this.oldUserScore = oldUserScore;
    }

    public BigDecimal getNewUserScore() {
        return newUserScore;
    }

    public void setNewUserScore(BigDecimal newUserScore) {
        this.newUserScore = newUserScore;
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
