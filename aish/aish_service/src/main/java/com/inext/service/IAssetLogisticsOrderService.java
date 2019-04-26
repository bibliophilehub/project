package com.inext.service;

import com.github.pagehelper.PageInfo;
import com.inext.entity.AssetLogisticsOrder;

import java.util.List;
import java.util.Map;

/**
 * 订单物流信息
 * 开发人员：wenkk
 * 创建时间：2017-07-26 09:45
 */
public interface IAssetLogisticsOrderService {

    PageInfo<AssetLogisticsOrder> getPageList(Map<String, Object> params);

    List<Map<String, String>> getList(Map<String, Object> params);

    int audit(Integer id, Integer auditStatus);

    int saveAssetLogisticsOrder(AssetLogisticsOrder assetLogisticsOrder);
}
