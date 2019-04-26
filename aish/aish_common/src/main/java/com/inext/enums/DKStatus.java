
package com.inext.enums;

/**
 * 
 * 功能描述: TODO 我方代扣处理状态
 * @编码实现人员 wxy0初始状态，1 处理中，2成功，3失败
 * @实现日期 2018年6月26日
 */
public enum DKStatus
{
        /**
         * 初始状态
         */
        INIT(0, "初始状态"),
        /**
         * 代扣处理中
         */
        DOING(1, "代扣处理中"),
        /**
         * 代扣成功
         */
        SUCCESS(2, "代扣成功"),
        /**
         * 代扣失败
         */
        FAIL(3, "代扣失败");

    private Integer value;

    private String desc;

    private DKStatus(final Integer value, final String desc)
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
        for (final DKStatus e : DKStatus.values())
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
