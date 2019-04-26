package com.inext.service;

import com.github.pagehelper.PageInfo;
import com.inext.entity.AbnormalOrder;
import com.inext.entity.AssetRepaymentOrder;

import java.util.List;
import java.util.Map;

/**
 */
public interface IAbonormalOrderService extends BaseSerivce<AssetRepaymentOrder> {

    PageInfo<AssetRepaymentOrder> getPageList(Map<String, Object> params);

    List<Map<String, String>> getAbnormalList(Map<String, Object> params);

    AbnormalOrder getAbnormalOrder(String noOrder);

    void saveAbnormal(AbnormalOrder abnormalOrder);
}
