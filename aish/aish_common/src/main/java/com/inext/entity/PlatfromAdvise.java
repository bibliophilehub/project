package com.inext.entity;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;


/**
 *
 * @version
 *
 */
public class PlatfromAdvise implements Serializable {

    private static final long serialVersionUID = 8098342754628169918L;
    @Id
    private Integer id;
    private String userPhone;
    private String adviseContent;
    private Date adviseAddtime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getAdviseContent() {
        return adviseContent;
    }

    public void setAdviseContent(String adviseContent) {
        this.adviseContent = adviseContent;
    }

    public Date getAdviseAddtime() {
        return adviseAddtime;
    }

    public void setAdviseAddtime(Date adviseAddtime) {
        this.adviseAddtime = adviseAddtime;
    }


}
