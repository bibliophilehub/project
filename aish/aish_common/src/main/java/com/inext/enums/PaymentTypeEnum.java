
package com.inext.enums;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 支付方式
 * @编码实现人员 user
 * @需求提出人员 TODO 填写需求填写人员
 * @实现日期 2018年7月12日
 */
public enum PaymentTypeEnum
{
        /**
         * 1邮寄手机
         */
        MAIL(1, "邮寄手机"),
        /**
         * 2后台手动
         */
        BACK(2, "后台手动"),
        /**
         * 3合利宝主动支付
         */
        HELI(3, "合利宝主动支付"),
        /**
         * 4易宝代扣支付
         */
        YOP_DK(4, "易宝代扣支付"),
        /**
         * 5先锋代扣支付
         */
        UCF_DK(5, "先锋代扣支付"),
        /**
        * 7微信支付
        */
        WEIPAY(7, "微信支付"),
        /**
        * 10支付宝支付
        */
        ALIPAY(10, "支付宝支付"),
        /**
         * 11后台支付宝还款
         */
        BACK_ALIPAY(11, "后台支付宝支付"),
        /**
         * 12 后台银行卡还款
         */
        BACK_BANKPAY(12, "后台银行卡支付"),
        /**
         * 13 后台微信还款
         */
        BACK_WEICHATPAY(13, "后台微信支付"),

        /**
         * 14新生代扣支付
         */
        XS_DK(14, "新生代扣支付");

    private Integer value;

    private String desc;

    private PaymentTypeEnum(final Integer value, final String desc)
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
        for (final PaymentTypeEnum e : PaymentTypeEnum.values())
        {
            if (e.getValue() == value)
            {
                return e.desc;
            }
        }
        return null;
    }

    /**
     * 把枚举类的所有对象都转化为map
     * @author user
     * @date 2018年7月12日
     * @return
     */
    public static Map<Integer, Object> getMapValues()
    {
        Map<Integer, Object> repaymentTypeMap = new LinkedHashMap<Integer, Object>();
        for (final PaymentTypeEnum e : PaymentTypeEnum.values())
        {
            repaymentTypeMap.put(e.getValue(), e.getDesc());
        }
        return repaymentTypeMap;
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
