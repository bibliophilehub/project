package com.inext.service;

import com.inext.entity.AssetOrderStatusHis;

import java.util.List;
import java.util.Map;

public interface IAssetOrderStatusHisService {

    AssetOrderStatusHis getLastOrderHis(Integer orderId);

    List<AssetOrderStatusHis> getOrderHisListByOrderId(Integer orderId);

    int saveHis(AssetOrderStatusHis his);
    
    void removeHis(Map<String, Object> params);
}
