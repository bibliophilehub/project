package com.inext.ppd;

/**
 * 返回代码
 */
public enum ReturnCode {

    /** 批次已接收 */
    SUCCESS("00000", "批次已接收"),
    /** 批次校验失败 */
    PPD_FAIL("00001", "批次校验失败"),
    /** 批次完成 */
    PPD_SUCCESS("00002", "批次完成"),
    /** 批次处理中 */
    PPD_WAIT("00003", "批次处理中");

    private String code;
    private String desc;

    ReturnCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ReturnCode getByCode(String code) {
        for (ReturnCode returnCode : values()) {
            if (code.equals(returnCode.getCode())) {
                return returnCode;
            }
        }
        return null;
    }



    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
