package com.inext.enums;

/**
 * @Author Administrator
 * @Create 2017-07-07 20:38
 **/
public enum RegisterRespEnum {

    FAIL_001("-1", "Fail"),
    FAIL_002("-2", "手机号已经存在");

    private String code;
    private String msg;

    RegisterRespEnum() {
    }

    RegisterRespEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
