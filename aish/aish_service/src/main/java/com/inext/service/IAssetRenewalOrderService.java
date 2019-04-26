package com.inext.service;

import com.github.pagehelper.PageInfo;
import com.inext.entity.AssetRenewalOrder;

import java.util.List;
import java.util.Map;

/**
 * 续期订单
 * 开发人员：wenkk
 * 创建时间：2017-07-26 09:45
 */
public interface IAssetRenewalOrderService {

    PageInfo<AssetRenewalOrder> getPageList(Map<String, Object> params);

    List<Map<String, String>> getList(Map<String, Object> params);

    int insertRenewalOrder(AssetRenewalOrder assetRenewalOrder);

    /**
     * 查询订单续期次数
     * @param params
     * @return
     */
    int getCount(Map<String, Object> params);
}
