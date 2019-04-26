package com.inext.constants;

/**
 * @author licheng
 * @version 1.0
 * @Title AuthCodeMsgConstant.java
 * @description 身份证认证返回对象定义
 * @time 2017年12月8日 下午4:52:49
 */
public enum AuthCodeMsgEnum {

    //'0 匹配 1 身份证与姓名不匹配 2 无此身份证号码'
    SUCCESS("认证成功", 0), FAIL_1("身份证与姓名不匹配", 1), FAIL_2("无此身份证号码", 2), OUT_NUMS("认证超过限定次数", 3), OUT_TIME("访问超时，请稍后尝试", 4), EXCEPTION("访问异常，请联系李成", 5), NO_URNNING("芝麻信用欺诈信息校验未启用", 6), UNVALID("身份证号码规则验证不通过", 7), FAIL("身份证认证失败", -1);

    // 成员变量
    private String msg;
    private int code;

    // 构造方法
    private AuthCodeMsgEnum(String msg, int code) {
        this.msg = msg;
        this.code = code;
    }

    // 普通方法
    public static String getMsg(int code) {
        for (AuthCodeMsgEnum c : AuthCodeMsgEnum.values()) {
            if (c.getCode() == code) {
                return c.msg;
            }
        }
        return null;
    }

    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }

    // 覆盖方法
    @Override
    public String toString() {
        return this.msg + "_" + this.code;
    }
}
  