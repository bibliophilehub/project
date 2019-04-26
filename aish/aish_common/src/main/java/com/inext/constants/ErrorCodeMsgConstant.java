package com.inext.constants;


/**
 * 错误信息常类
 *
 * @author user
 */
public class ErrorCodeMsgConstant {

    public final static String CODE_SUCCESS = "0000";
    public final static String MSG_SUCCESS = "操作成功";

    public final static String CODE_SYSTEM_ERROR = "9999";
    public final static String MSG_SYSTEM_ERROR = "系统繁忙，请稍候再试";


    public final static String CODE_USER_NO_FIND = "0001";
    public final static String MSG_USER_NO_FIND = "没有查到此用户";

    public final static String CODE_PASSWORD_ERROR = "0002";
    public final static String MSG_PASSWORD_ERROR = "密码错误";

    public final static String CODE_USER_PARA_ERROR = "0003";
    public final static String MSG_USER_PARA_ERROR = "请求参数错误";

    public final static String CODE_MERCHANT_NO_FIND = "0004";
    public final static String MSG_MERCHANT_NO_FIND = "没有查到此商户";

    public final static String CODE_MERCHANT_DEPOSIT_ERROR = "0005";
    public final static String MSG_MERCHANT_DEPOSIT_ERROR = "提现金额不足";

    public final static String CODE_SIGN_ERROR = "0006";
    public final static String MSG_SIGN_ERROR = "验签失败";

    public final static String CODE_DECRYPT_ERROR = "0007";
    public final static String MSG_DECRYPT_ERROR = "解密失败";

    public final static String CODE_MOBILECODE_ERROR = "0008";
    public final static String MSG_MOBILECODE_ERROR = "短信验证码错误";

}
