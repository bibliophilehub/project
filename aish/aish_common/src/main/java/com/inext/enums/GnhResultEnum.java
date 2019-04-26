
package com.inext.enums;

/**
 * 给你花联合注册使用
 * 返回码
 */
public enum GnhResultEnum
{

    /**
     * 撞库接口返回码
     */
    NEW_USER(200,"新用户"),
    GNH_OLD_USER(1000,"本渠道老用户"),
    OTHER_OLD_USER(1004,"其他渠道老用户"),
    SYS_ERROR(1002,"服务内部错误"),
    SIGN_ERROR(1003,"验证签名错误"),


    /**
     * 注册接口返回码
     */
    REGISTER_SUCCESS(200,"成功"),
    REGISTER_FAILURE(1001,"失败");


    private Integer code;
    private String msg;

    GnhResultEnum() {
    }

    GnhResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
