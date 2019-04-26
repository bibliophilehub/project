
package com.inext.entity.zm;

import java.io.Serializable;

public class SesameDto implements Serializable
{

    private static final long serialVersionUID = 1141519021429874779L;

    private String transId; //  商户订单id
    private String realname; //   姓名
    private String idCard; // 身份证
    private String userPhone; //  手机号
    private String time;
    private String score; // 芝麻分
    public String getTransId()
    {
        return transId;
    }
    public void setTransId(String transId)
    {
        this.transId = transId;
    }
    public String getRealname()
    {
        return realname;
    }
    public void setRealname(String realname)
    {
        this.realname = realname;
    }
    public String getIdCard()
    {
        return idCard;
    }
    public void setIdCard(String idCard)
    {
        this.idCard = idCard;
    }
    public String getUserPhone()
    {
        return userPhone;
    }
    public void setUserPhone(String userPhone)
    {
        this.userPhone = userPhone;
    }
    public String getTime()
    {
        return time;
    }
    public void setTime(String time)
    {
        this.time = time;
    }
    public String getScore()
    {
        return score;
    }
    public void setScore(String score)
    {
        this.score = score;
    }
}
