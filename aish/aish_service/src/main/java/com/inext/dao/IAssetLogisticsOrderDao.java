package com.inext.dao;

import com.inext.configuration.BaseDao;
import com.inext.entity.AssetLogisticsOrder;

import java.util.List;
import java.util.Map;


/**
 * 订单物流信息
 * 开发人员：wenkk
 * 创建时间：2018-03-21 09:26
 */
public interface IAssetLogisticsOrderDao extends BaseDao<AssetLogisticsOrder> {

    public List<AssetLogisticsOrder> getPageList(Map<String, Object> params);

    public List<Map<String, String>> getList(Map<String, Object> params);
}
