package com.inext.service;

import java.util.Date;

/**
 * Created by user on 2018/1/6.
 */
public interface IChannelStatisticsPerdayService {

	void channelStatistics(Date today);
	
	void channelBalanceStatistics(Date today);


}
