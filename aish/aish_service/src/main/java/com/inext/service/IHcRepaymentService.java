package com.inext.service;

import com.inext.entity.fuyou.Fuiou;

import java.util.Map;

/**
 * 汇潮service
 */
public interface IHcRepaymentService {

    int doCcallback(Map<String, String> params);

    void setOrderHisStauts(String repaymentInfoId);

    Map<String,Object> getRepaymentParams(Integer userId, Integer orderId, Integer type, String deviceType);

    Map<String,Object> queryHuiChaoOrder(Integer orderId);
}
