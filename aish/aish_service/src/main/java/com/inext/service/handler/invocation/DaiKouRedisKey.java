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

package com.inext.service.handler.invocation;

/**
 * Message Topic
 */
public enum DaiKouRedisKey
{

        /**
         * 易宝
         */
        YOP("doYOPDKTask", "易宝"),
        /**
         * 易宝代扣查询
         */
        YOP_QUERY("doYOPDK_Query_Task", "易宝代扣查询"),
        /**
         * 易宝代扣鉴权绑卡
         */
        YOP_AUTO_BIND("doYOP_BindCard_Task", "易宝代扣鉴权绑卡"),
        /**
         * 代扣
         */
        DK("doDKTask", "扣款"),
        /**
         * 代扣查询
         */
        DK_QUERY("doDK_Query_Task", "代扣查询");

    private String code;

    private String desc;

    /**
     * @param code 消息编码
     * @param desc 消息描述
     */
    private DaiKouRedisKey(final String code, final String desc)
    {
        this.code = code;
        this.desc = desc;

    }

    /**
     * @param code
     * @return desc
     */
    public static String getExtNameByCode(final String code)
    {
        for (final DaiKouRedisKey e : DaiKouRedisKey.values())
        {
            if (e.getCode().equals(code))
            {
                return e.desc;
            }
        }
        return null;
    }

    /**
     * @return code
     */
    public String getCode()
    {
        return code;
    }

    /**
     * @param code
     */
    public void setCode(final String code)
    {
        this.code = code;
    }

    /**
     * @return desc
     */
    public String getDesc()
    {
        return desc;
    }

    /**
     * @param desc
     */
    public void setDesc(final String desc)
    {
        this.desc = desc;
    }

}
