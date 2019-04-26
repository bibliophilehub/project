package com.inext.dao;


import com.inext.configuration.BaseDao;
import com.inext.entity.ChannelStatisticsLog;

import java.util.List;
import java.util.Map;

/**
 * Created by user on 2018/1/6.
 */
public interface IChannelStatisticsLogDao extends BaseDao<ChannelStatisticsLog> {
	
	List<Map<String,Object>> getchannelStatisticsData(Map<String, Object> param);
}
