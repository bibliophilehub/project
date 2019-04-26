package com.inext.dao;


import com.inext.configuration.BaseDao;
import com.inext.entity.ChannelBalanceStatistics;

import java.util.List;
import java.util.Map;

/**
 * Created by user on 2018/1/6.
 */
public interface IChannelBalanceStatisticsDao extends BaseDao<ChannelBalanceStatistics> {

	void deleteOldData(Map<String, Object> param);

	void addBalanceStatistics(List<Map<String, Object>> datas);

	List<ChannelBalanceStatistics> getBalanceStatisticsList(Map<String, Object> params);

	Map<String, Object> findBalanceStatistics(Map<String, Object> params);
	
}
