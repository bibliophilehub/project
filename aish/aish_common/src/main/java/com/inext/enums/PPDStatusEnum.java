
package com.inext.enums;

/**
 * 扣款明细状态
 */
public enum PPDStatusEnum
{

    KK_SUCCESS("01001","扣款成功"),
    KK_FAIL("01002","扣款失败"),
    KK_WAIT("01003","扣款处理中"),
    CHECK_FAIL("01004","校验失败"),
    REVIEW_WAIT("01005","复核中"),
    REVIEW_FAIL("01006","复核失败"),
    MESSAGE_WAIT("01007","短信授权中"),
    MESSAGE_FAIL("01008","短信授权拒绝"),
    MESSAGE_MONEY_WAIT("01010","短信金额获取中");

    private String code;
    private String msg;

    PPDStatusEnum() {
    }

    PPDStatusEnum(String code, String msg) {
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
