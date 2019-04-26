/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 */

package com.inext.enums;

/**
 * Message Topic
 */
public enum DaiKouBusinessType
{

        /**
         * 易宝
         */
        YOP("yop", 1, "易宝", true),

        /**
         * 先锋
         */
        UCF("ucf", 2, "先锋", false),
    /**
     * 新生
     */
    XFP("xfp", 3, "新生", false),

        /**
         * 其他
         */
        OTHER("other", 0, "其他", false);

    private String code;

    private Integer value;

    private String desc;

    private Boolean isProcess;

    /**
     * @param code 消息编码
     * @param desc 消息描述
     * @param isProcess 消息类型是否废弃
     */
    private DaiKouBusinessType(final String code, final Integer value, final String desc, final boolean isProcess)
    {
        this.code = code;
        this.desc = desc;
        this.value = value;
        this.isProcess = isProcess;

    }

    /**
     * @param code
     * @return desc
     */
    public static String getExtNameByCode(final String code)
    {
        for (final DaiKouBusinessType e : DaiKouBusinessType.values())
        {
            if (e.getCode().equals(code))
            {
                return e.desc;
            }
        }
        return null;
    }

    /**
     * @param value
     * @return code
     */
    public static String getCodeByVaule(final Integer value)
    {
        for (final DaiKouBusinessType e : DaiKouBusinessType.values())
        {
            if (e.value.intValue() == value.intValue())
            {
                return e.code;
            }
        }
        return null;
    }

    /**
     * @param code
     * @return isProcess
     */
    public static Boolean isExistProcess(final String code)
    {
        for (final DaiKouBusinessType e : DaiKouBusinessType.values())
        {
            if (e.getCode().equals(code))
            {
                return e.isProcess;
            }
        }
        return false;
    }

    /**
     * @return code
     */
    public String getCode()
    {
        return code;
    }

    public Integer getValue()
    {
        return value;
    }

    /**
     * @return desc
     */
    public String getDesc()
    {
        return desc;
    }
}
