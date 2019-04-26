
package com.inext.enums;

/**
 * 
 * 功能描述: TODO 易宝返回状态值
 * @编码实现人员 wxy
 * @实现日期 2018年6月26日
 */
public enum YOPStatus
{
        /**
         * 待短验确认 
         */
        TO_VALIDATE("TO_VALIDATE", "待短验确认"),
        /**
         * 绑卡成功
         */
        BIND_SUCCESS("BIND_SUCCESS", "绑卡成功"),
        /**
         * 绑卡失败
         */
        BIND_FAIL("BIND_FAIL", "绑卡失败"),
        /**
         * 绑卡异常(可重试)
         */
        BIND_ERROR("BIND_ERROR", "绑卡异常"),
        /**
         * 系统异常
         */
        FAIL("FAIL", "系统异常"),
        /**
         * 已接收
         */
        ACCEPT("ACCEPT", "已接收"),
        /**
         * 处理中
         */
        PROCESSING("PROCESSING", "处理中"),
        /**
         * 支付成功
         */
        PAY_SUCCESS("PAY_SUCCESS", "支付成功"),
        /**
         * 超时失败
         */
        TIME_OUT("TIME_OUT", "超时失败"),
        /**
         * 支付失败
         */
        PAY_FAIL("PAY_FAIL", "支付失败");

    private String value;

    private String desc;

    private YOPStatus(final String value, final String desc)
    {
        this.value = value;
        this.desc = desc;
    }

    /**
    * 根据value获取desc TODO 增加功能描述
    * 
    * @author HOLI
    * @date Jun 19, 2017
    * @param value
    *            value
    * @return String
    */
    public static String getDescByValue(final String value)
    {
        for (final YOPStatus e : YOPStatus.values())
        {
            if (e.getValue().equals(value))
            {
                return e.desc;
            }
        }
        return null;
    }

    public String getValue()
    {
        return value;
    }

    public String getDesc()
    {
        return desc;
    }

}
