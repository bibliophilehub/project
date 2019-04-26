package com.inext.dao;

import com.inext.configuration.BaseDao;
import com.inext.entity.AbnormalOrder;
import com.inext.entity.HcDfInfo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface IAbnormalOrderDao extends BaseDao<AbnormalOrder> {

    AbnormalOrder getAbnormalOrder(String noOrder);

    public List<AbnormalOrder> getAbnormalOrderList(Map<String, Object> params);


    List<Map<String, String>> getList(Map<String, Object> params);

    Long getOrderByRefundOrderNo(String refundOrderNo);

    /**
     * 查询代付提现记录
     * @param
     * @return
     */
    List<HcDfInfo> getHcDfInfoList(Map<String, Object> params);
}
