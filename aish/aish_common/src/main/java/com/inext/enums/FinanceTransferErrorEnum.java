/**
 * fshows.com
 * Copyright (C) 2013-2018 All Rights Reserved.
 */
package com.inext.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 返回错误码
 * @author shensiping
 * @version FinanceTransferErrorEnum.java, v 0.1 2018-10-09 11:58
 */
public enum FinanceTransferErrorEnum {

    // 成功
    SUCCESS("0","成功"),
    COMMON_OPERATION_FAILED("COMMON_OPERATION_FAILED","操作失败"),
    COMMON_PARAM_MISSING("COMMON_PARAM_MISSING","缺少参数[%s]"),
    COMMON_METHOD_NOTSUPPORTED("COMMON_METHOD_NOTSUPPORTED","接口不允许%s请求"),
    COMMON_PARAM_INVALID("COMMON_PARAM_INVALID","参数校验错误[%s]"),
    COMMON_ACCESS_DENIED("COMMON_ACCESS_DENIED","访问权限不足[%s]"),
    COMMON_PARAMPATTERN_INVALID("COMMON_PARAMPATTERN_INVALID","参数格式错误[%s]"),
    COMMON_SYSTEM_ERROR("COMMON_SYSTEM_ERROR","服务器异常"),
    COMMON_UNKNOWN_ERROR("COMMON_UNKNOWN_ERROR","未知错误"),
    COMMON_NOT_LOGIN("COMMON_NOT_LOGIN","用户未登录"),
    COMMON_KICK_OUT("COMMON_KICK_OUT","用户被踢下线"),
    COMMON_PARSE_XML_ERROR("COMMON_PARSE_XML_ERROR","解析XML异常"),
    COMMON_CONNECT_TIMEOUT_ERROR("COMMON_CONNECT_TIMEOUT_ERROR","连接超时"),
    COMMON_CONNECT_FORMAT_ERROR("COMMON_CONNECT_FORMAT_ERROR","报文格式错误"),
    COMMON_NOT_RETURN_VALUE("COMMON_NOT_RETURN_VALUE","此账户昨日没有打款记录"),

    // API
    API_APPLY_TYPE_CONFIG_EMPTY("API_APPLY_TYPE_CONFIG_EMPTY", "业务类型未配置"),
    API_SIGN_VERIFY_ERROR("API_SIGN_VERIFY_ERROR", "验签失败"),
    API_APPLY_LIST_EMPTY("API_APPLY_LIST_EMPTY", "请求列表为空"),
    API_APPLY_LIST_OVER_MAX("API_APPLY_LIST_OVER_MAX", "请求列表总数超过最大值[%s]"),
//    API_APPLY_CODE_DUPLICATE("API_APPLY_CODE_DUPLICATE", "流水号重复"),
    API_APPLY_TYPE_NOT_SET("API_APPLY_TYPE_NOT_SET", "业务类型参数未知"),
    API_APPLY_ORIGIN_NOT_EXIST("API_APPLY_ORIGIN_NOT_EXIST", "原流水不存在"),
    API_APPLY_ORIGIN_PAID("API_APPLY_ORIGIN_PAID", "关联流水已出款成功"),
    API_APPLY_ORIGIN_PAYING("API_APPLY_ORIGIN_PAYING", "关联流水未出款或出款中"),
    API_APPLY_ORIGIN_FREEZE("API_APPLY_ORIGIN_FREEZE", "关联流水已被冻结"),
    API_APPLY_BANKNO_REQUIRED("API_APPLY_BANKNO_REQUIRED", "对公联行号必填"),
    API_APPLY_BANKADDRESS_REQUIRED("API_APPLY_BANKADDRESS_REQUIRED", "跨行开户地必填"),
    API_APPLY_MONEY_ERROR("API_APPLY_MONEY_ERROR", "金额必须大于零"),
    API_CALLBACK_ERROR("API_CALLBACK_ERROR", "业务回调异常"),


    // 登录
    LOGIN_USER_NAME_NOTNULL("LOGIN_USER_NAME_NOTNULL","用户名不能为空"),
    LOGIN_PASSWORD_NOTNULL("LOGIN_PASSWORD_NOTNULL","密码不能为空"),
    LOGIN_CAPTCHA_NOTNULL("LOGIN_CAPTCHA_NOTNULL","验证码不能为空"),
    LOGIN_ACCOUNT_NOT_EXIST("LOGIN_ACCOUNT_NOT_EXIST","用户不存在"),
    LOGIN_CAPTCHA_ERROR("LOGIN_CAPTCHA_ERROR","验证码错误"),
    LOGIN_ACCOUNT_LOCKED("LOGIN_ACCOUNT_LOCKED","用户被锁定"),
    LOGIN_ACCOUNT_FORBIDDEN("LOGIN_ACCOUNT_FORBIDDEN","用户被禁止登陆"),
    LOGIN_PARAM_PWD_INVALID("LOGIN_PARAM_PWD_INVALID","密码校验错误无效密码"),
    LOGIN_USERNAME_PWD_ERROR("LOGIN_USERNAME_PWD_ERROR","用户名或密码错误"),
    LOGIN_ERROR("LOGIN_ERROR","登录失败"),
    LOGIN_USER_ROLE_FORBIDDEN("LOGIN_USER_ROLE_FORBIDDEN","该用户的角色被禁用"),
    USER_ACCESS_DENIED("USER_ACCESS_DENIED","访问权限不足"),
    USER_GRANT_CHANGED("USER_GRANT_CHANGED","用户角色权限已被修改"),


    // 招商银行
    CMB_GET_ACCOUNT_ERROR("CMB_GET_ACCOUNT_ERROR","获取账户信息失败"),
    CMB_NOT_ENOUGH_MONEY("CMB_NOT_ENOUGH_MONEY","余额不足，无法打款"),

    APPLY_NOT_EXISTS("APPLY_NOT_EXISTS","该笔打款不存在或状态异常"),
    BUSINESS_CODE_NAME_NOTNULL("BUSINESS_CODE_NAME_NOTNULL","系统流水号不能为空"),
    BUSINESS_CODE_NAME_NOTFIND("BUSINESS_CODE_NAME_NOTFIND","没有该系统流水号信息"),
    BUSINESS_CODE_NOT_STATUS("BUSINESS_CODE_NOT_STATUS","没有该流水号或流水已处理"),
    PAY_ORDER_CODE_NAME_NOTFIND("PAY_ORDER_CODE_NAME_NOTFIND","没有该银行流水号信息"),
    PAY_IN_OPERATE("PAY_IN_OPERATE","请不要重复打款"),

    APPLY_STATUS_ERROR("APPLY_STATUS_ERROR","系统流水号打款状态已被修改"),
    CHANNEL_EXISTS("CHANNEL_EXISTS","通道一致无需修改"),
    CHANNEL_NOT_USE("CHANNEL_NOT_USE","通道不可用"),
    APPLY_NOT_USE("APPLY_NOT_USE","该流水已有打款状态"),
    APPLY_NOT_DATA("APPLY_NOT_DATA","没有统计数据"),


    // 业务方接口
    APPLY_LIST_EMPTY("APPLY_LIST_EMPTY","出款列表为空"),



    ;


    private String code;
    private String message;

    private FinanceTransferErrorEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static FinanceTransferErrorEnum getErrorEnum(String code) {
        for (FinanceTransferErrorEnum errorEnum : values()) {
            if (StringUtils.equals(code, errorEnum.getCode())) {
                return errorEnum;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}