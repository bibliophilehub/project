package com.inext.entity;

import javax.persistence.Id;
import java.util.Date;

/**
 * 开发人员：jzhang
 * 创建时间：2017-05-08 下午 14:46
 */
public class BatchUnSuc {

    @Id
    protected Integer id;
    protected String code;
    protected String msg;
    protected String keyword;//关键字
    protected Integer counts;//查询次数
    protected Date createDate;
    protected String remarks;
    protected boolean isSuc; //是否成功 0 否 1是


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getCounts() {
        return counts;
    }

    public void setCounts(Integer counts) {
        this.counts = counts;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public boolean isSuc() {
        return isSuc;
    }

    public void setSuc(boolean suc) {
        isSuc = suc;
    }
}
