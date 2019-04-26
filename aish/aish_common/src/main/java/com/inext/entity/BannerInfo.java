package com.inext.entity;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;


/**
 *
 * @version
 *
 */
public class BannerInfo implements Serializable {

    private static final long serialVersionUID = 8098342754628169918L;
    @Id
    private Integer id;
    private String url;
    private String title;
    private String reurl;
    private Integer sort;
    private Integer status;
    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReurl() {
        return reurl;
    }

    public void setReurl(String reurl) {
        this.reurl = reurl;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


}
