package com.inext.service.impl;

import com.inext.dao.IChannelDao;
import com.inext.dao.IChannelStatisticsLogDao;
import com.inext.service.IChannelStatisticsLogService;
import com.inext.util.StatisticsChannelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 借条业务接口
 */
@Service
public class ChannelStatisticsLogService implements IChannelStatisticsLogService {

    @Autowired
    private IChannelDao channelDao;
    @Autowired
    private IChannelStatisticsLogDao channelStatisticsLogDao;
    
	@Override
	public void addChannleStatistics(String channelCode, int type,Integer channelId) {
		
		new StatisticsChannelUtil(channelCode,type,channelStatisticsLogDao,channelId).start();
	}


}
