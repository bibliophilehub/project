
package com.inext.service.handler;

import com.inext.result.ApiServiceResult;
import com.inext.service.handler.invocation.DKParam;

/**
 * 
 * Base Payment Handler
 * @编码实现人员 lihong
 * @需求提出人员 TODO 填写需求填写人员
 * @实现日期 2018年6月25日
 */
public interface BasePaymentHandler
{

    /**
     * 执行代扣
     * @throws Exception
     */
    public ApiServiceResult<Object> execute(DKParam request) throws Exception;

    /**
     * 执行代扣
     * @throws Exception
     */
    public ApiServiceResult<Object> query(DKParam request) throws Exception;
}
