
package com.inext.service.handler.items;

import com.inext.result.ApiServiceResult;
import com.inext.service.handler.invocation.DKParam;

/**
 * 支付基类 
 * @编码实现人员 lihong
 * @需求提出人员 TODO 填写需求填写人员
 * @实现日期 2018年6月25日
 */
public abstract class BasicPayItem
{
    /**
     * 获取支付消息类型
     *
     * @return Message type
     */
    public abstract String getMessageTopic();

    /**
     * 执行支付请求
     * @param request
     */
    public abstract ApiServiceResult<Object> excute(DKParam request) throws Exception;

    /**
     * 执行支付查询
     * @param request
     */
    public abstract ApiServiceResult<Object> query(DKParam request) throws Exception;
}
