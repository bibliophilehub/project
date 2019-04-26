package com.inext.enumerate;

/**
 * 定义返回状态
 */
public enum ApiStatus {

    /**
     * 成功
     */
    SUCCESS("0", "操作成功"),
    /**
     * 成功
     */
    SUCCESS_GXB("1", "操作成功"),
    /**
     * 暂无数据
     */
    NO_DATA("0", "暂无数据"),
    /**
     * 失败
     */
    FAIL("-1", "操作失败"),
    /**
     * 系统错误
     */
    ERROR("500", "系统繁忙,请稍后再试"),
    /**
     * 处理中
     */
    DEALING("100", "处理中"),
    /**
     * 代付的订单发生了退款
     */
    REFUND("001", "退款");
    /**
     * 名称
     */
    private final String code;

    /**
     * 值
     */
    private final String value;

    ApiStatus(String code, String value) {
        this.code = code;
        this.value = value;
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
    public String getValue() {
        return value;
    }
}
