package com.inext.dao;

import com.inext.configuration.BaseDao;
import com.inext.entity.AssetRepaymentOrderDetail;


/**
 * 还款流水详情 
 * @编码实现人员 lihong
 * @需求提出人员 TODO 填写需求填写人员
 * @实现日期 2018年6月15日
 */
public interface IAssetRepaymentOrderDetailDao extends BaseDao<AssetRepaymentOrderDetail> {
    
    /**
     * 根据还款订单查询历史所有的各项还款总额 
     * @author lihong
     * @date 2018年6月15日
     * @return
     */
    AssetRepaymentOrderDetail selectDetailsByRepaymentOrderId(Integer repaymentOrderId);
}
