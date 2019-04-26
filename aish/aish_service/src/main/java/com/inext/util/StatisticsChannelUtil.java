package com.inext.util;

import com.inext.dao.IChannelStatisticsLogDao;
import com.inext.entity.ChannelStatisticsLog;
import com.inext.utils.DateUtils;

import java.text.ParseException;
import java.util.Date;

public class StatisticsChannelUtil extends Thread {
	
	private String channelCode;
	private int type;//'1:pv ;2:uv
	private IChannelStatisticsLogDao channelStatisticsLogDao;
	private Integer channelId;
	
	
	
	public StatisticsChannelUtil(String channelCode, int type, IChannelStatisticsLogDao channelStatisticsLogDao, Integer channelId) {
		super();
		this.channelCode = channelCode;
		this.type = type;
		this.channelStatisticsLogDao = channelStatisticsLogDao;
		this.channelId = channelId;
	}



	public void run()
	{
		try {
			
			ChannelStatisticsLog csl=new ChannelStatisticsLog();
			csl.setChannelId(channelId);
			csl.setType(type);
			csl.setStatisticsDate(DateUtils.convertStringToDate(DateUtils.formatDate(new Date(), DateUtils.DEFAULT_FORMAT)));
			channelStatisticsLogDao.insert(csl);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	

}
