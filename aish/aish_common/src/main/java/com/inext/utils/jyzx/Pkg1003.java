package com.inext.utils.jyzx;

public class Pkg1003 {
    private String realName; // 姓名
    private String idCard; // 身份证号

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    @Override
    public String toString() {
        return "Pkg1003 [idCard=" + idCard + ", realName=" + realName + "]";
    }

}
