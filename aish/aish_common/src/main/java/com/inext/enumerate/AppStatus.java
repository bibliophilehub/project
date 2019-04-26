package com.inext.enumerate;

/**
 * 定义返回状态
 */
public enum AppStatus {

    /**
     * 成功
     */
    SUCCESS("0", "成功"),
    /**
     * 失败
     */
    FAIL("-1", "失败"),
    /**
     * 系统错误
     */
    ERROR("500", "系统繁忙,请稍后再试"),
    /**
     * 未登录
     */

    LOGIN("300", "未登录");
    /**
     * 名称
     */
    private final String code;

    /**
     * 值
     */
    private final String desc;

    AppStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 获取名称
     *
     * @return
     */
    public String getCode() {
        return code;
    }

    /**
     * 获取值
     *
     * @return
     */
    public String getDesc() {
        return desc;
    }
}
