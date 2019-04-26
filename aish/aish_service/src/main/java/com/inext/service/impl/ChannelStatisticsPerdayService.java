package com.inext.service.impl;

import com.inext.dao.IChannelBalanceStatisticsDao;
import com.inext.dao.IChannelDao;
import com.inext.dao.IChannelStatisticsLogDao;
import com.inext.dao.IChannelStatisticsPerdayDao;
import com.inext.service.IChannelStatisticsPerdayService;
import com.inext.utils.DateUtil;
import com.inext.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 借条业务接口
 */
@Service
public class ChannelStatisticsPerdayService implements IChannelStatisticsPerdayService {

    @Autowired
    private IChannelDao channelDao;
    @Autowired
    private IChannelBalanceStatisticsDao channelBalanceStatisticsDao;
    @Autowired
    private IChannelStatisticsLogDao channelStatisticsLogDao;
    @Autowired
    private IChannelStatisticsPerdayDao channelStatisticsPerdayDao;
	@Override
	public void channelStatistics(Date today) {
		Date yesterday= null;
		if(null!=today){
			yesterday=today;
		}else{
			yesterday=DateUtil.addDay(new Date(), -1);
		}

        Map<String, Object> param=new HashMap<String,Object>();
        param.put("statisticsDate", yesterday);
        List<Map<String,Object>> datas=channelStatisticsLogDao.getchannelStatisticsData(param);
        if(null!=datas&&datas.size()>0){
        	channelStatisticsPerdayDao.deleteOldData(param);
        	channelStatisticsPerdayDao.addChannelDataPerDay(datas);
        }
        
	}
	@Override
	public void channelBalanceStatistics(Date today) {
		Date yesterday= null;
		if(null!=today){
			yesterday=today;
		}else{
			yesterday=DateUtil.addDay(new Date(), -1);
		}

        try {
			Map<String, Object> param=new HashMap<String,Object>();
			param.put("statisticsDate", yesterday);
			List<Map<String,Object>> datas=channelDao.channelBalanceStatistics(param);
			if(null!=datas&&datas.size()>0){
				channelBalanceStatisticsDao.deleteOldData(param);
				
				yesterday=DateUtils.convertStringToDate(DateUtils.formatDate(yesterday, DateUtils.DEFAULT_FORMAT));
				for(Map<String,Object> data: datas){
					data.put("statisticsDate", yesterday);
				}
				
				channelBalanceStatisticsDao.addBalanceStatistics(datas);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	}
	

}
