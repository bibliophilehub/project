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
 * 渠道借款额度设置表
 * @version
 *
 */
@Data
public class ChannelLoanQuota implements Serializable {
    @Id
//    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @GeneratedValue(generator = "JDBC")
    private Integer id;
    private Integer channelId;
    private String loanPer;
    private String loanWy;
    private String loanAll;
    private Date createTime;
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public String getLoanPer() {
        return loanPer;
    }

    public void setLoanPer(String loanPer) {
        this.loanPer = loanPer;
    }

    public String getLoanWy() {
        return loanWy;
    }

    public void setLoanWy(String loanWy) {
        this.loanWy = loanWy;
    }

    public String getLoanAll() {
        return loanAll;
    }

    public void setLoanAll(String loanAll) {
        this.loanAll = loanAll;
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
