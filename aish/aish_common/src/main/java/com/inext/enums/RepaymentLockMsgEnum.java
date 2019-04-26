
package com.inext.enums;

/**
 * 用户还款 并发锁 机制提示
 * @编码实现人员 user
 * @需求提出人员 TODO 填写需求填写人员
 * @实现日期 2018年7月9日
 */
public enum RepaymentLockMsgEnum
{
        /**
         * 微信支付
         */
        WECHAT(1, "您正在微信支付中"),
        /**
         * 支付宝支付
         */
        ALIPAY(2, "您当前正在支付宝支付中"),
        /**
         * 快捷支付
         */
        QUICK_PAY(3, "您正在快捷支付"),
        /**
         * 系统代扣
         */
        SYSTEM_DK(4, "系统正在处理您的订单");

    private Integer value;

    private String desc;

    private RepaymentLockMsgEnum(final Integer value, final String desc)
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
    public static String getDescByValue(final Integer value)
    {
        for (final RepaymentLockMsgEnum e : RepaymentLockMsgEnum.values())
        {
            if (e.getValue() == value)
            {
                return e.desc;
            }
        }
        return null;
    }

    public Integer getValue()
    {
        return value;
    }

    public String getDesc()
    {
        return desc;
    }

}
