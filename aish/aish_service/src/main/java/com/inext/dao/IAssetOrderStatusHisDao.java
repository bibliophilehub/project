package com.inext.dao;

import com.inext.configuration.BaseDao;
import com.inext.entity.AssetOrderStatusHis;

import java.util.List;
import java.util.Map;


/**
 * 订单状态历史
 * 开发人员：wenkk
 * 创建时间：2018-03-21 09:26
 */
public interface IAssetOrderStatusHisDao extends BaseDao<AssetOrderStatusHis> {

    AssetOrderStatusHis getLastOrderHis(Integer orderId);

    List<AssetOrderStatusHis> getOrderHisListByOrderId(Integer orderId);

    List<AssetOrderStatusHis> getAuditRecordList(Map<String, Object> params);
    
    void removeHis(Map<String, Object> params);

}
