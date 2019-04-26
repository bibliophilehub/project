package com.inext.service;

import com.github.pagehelper.PageInfo;
import com.inext.entity.PaymentChannel;
import com.inext.result.ApiServiceResult;

import java.util.Map;

/**
 * @author lisige
 */
public interface IPaymentChannelService {

    /**
     * 查询分页
     *
     * @param params
     * @return
     */
    PageInfo<PaymentChannel> selectPaging(Map<String, Object> params);


    /**
     * 修改
     *
     * @param paymentChannel
     * @return
     */
    ApiServiceResult update(PaymentChannel paymentChannel);
}
