package com.inext.dao;

import com.inext.configuration.BaseDao;
import com.inext.entity.AssetRenewalOrder;

import java.util.List;
import java.util.Map;


/**
 * 续期订单
 * 开发人员：wenkk
 * 创建时间：2018-03-21 09:26
 */
public interface IAssetRenewalOrderDao extends BaseDao<AssetRenewalOrder> {

    public List<AssetRenewalOrder> getPageList(Map<String, Object> params);

    public List<Map<String, String>> getList(Map<String, Object> params);

    /**
     * 查询订单续期次数
     * @param params
     * @return
     */
    int getCount(Map<String, Object> params);
}
