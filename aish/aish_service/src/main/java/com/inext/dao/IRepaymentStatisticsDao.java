package com.inext.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.inext.entity.AssetRepaymentOrder;

@Repository
public interface IRepaymentStatisticsDao {


    List<Map<String, Object>> getRepaymentStatistics(Map<String, Object> params);

    List<AssetRepaymentOrder> queryStatic(Map<String, Object> params);

}
