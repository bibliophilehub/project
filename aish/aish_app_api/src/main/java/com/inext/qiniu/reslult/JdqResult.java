package com.inext.qiniu.reslult;


import net.sf.json.JSONObject;

public class JdqResult {

    public static String SUCCESS = "0";
    public static String FAIL = "-1";
    public static String ERROR = "-2";
    public static int CAN_LOAN = 1;
    public static int CAN_NOT_LOAN = 0;
    public static int TYPE_JDQ_OLD_USER = 0;//0-借点钱老用户 (定义：由借点钱渠道导流到合作方的用户，非第一次申请)
    public static int TYPE_NEW_USER = 1;//1-新用户 （定义：由借点钱渠道导流到合作方的用户，第一次申请）
    public static int TYPE_GNY_OLD_USER = 2;//2-合作方老用户 （定义：该用户为机构原有用户，且非借点钱渠道注册）


    /**
     * 返回代码
     */
    private String code = SUCCESS;// 默认成功

    /*
     * 提示信息
     */
    private String desc = "操作成功"; // 默认提示语

    private JSONObject data;

    public JdqResult() {
    }

    public JdqResult(String code, String desc, JSONObject data) {
        this.code = code;
        this.desc = desc;
        this.data = data;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


}
