package com.inext.service;

import com.inext.entity.AssetBorrowOrder;
import com.inext.result.ApiServiceResult;

/**
 * 创建口袋理财资产订单接口
 * @author lisige
 */
public interface IKouDaiActivoOrderService {

    /**
     * 创建资产订单
     * @param assetBorrowOrder
     * @return
     */
    ApiServiceResult createActivoOrder(AssetBorrowOrder assetBorrowOrder);
}
